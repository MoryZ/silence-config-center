package com.old.silence.config.center.domain.service;


import jakarta.annotation.PreDestroy;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.old.silence.config.center.domain.service.event.EventStrategyFactory;
import com.old.silence.config.center.enums.EventType;


import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author moryzang
 */
@Service
public class LongPollingService {

    private static final Logger logger = LoggerFactory.getLogger(LongPollingService.class);
    private final Map<String, AsyncContext> contextsMap = new ConcurrentHashMap<>();
    private final EventStrategyFactory eventStrategyFactory;
    private final ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1);

    public LongPollingService(EventStrategyFactory eventStrategyFactory) {
        this.eventStrategyFactory = eventStrategyFactory;
    }

    /**
     * 添加订阅者
     */
    public void subscribeConfig(String group, String appId, String namespace,
                                HttpServletRequest request, HttpServletResponse response) {
        String key = String.join("-", appId, group, namespace);
        AsyncContext context = request.startAsync();

        // 设置超时
        context.setTimeout(30000L);

        // 添加超时监听器
        context.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) {
                contextsMap.remove(key, context); // 完成后移除
            }

            @Override
            public void onTimeout(AsyncEvent event) {
                try {
                    // 返回 304 状态码表示未修改
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                } catch (Exception e) {
                    logger.warn("设置超时响应状态失败, key={}", key, e);
                } finally {
                    context.complete();
                    contextsMap.remove(key, context);
                }
            }

            @Override
            public void onError(AsyncEvent event) {
                contextsMap.remove(key, context);
            }

            @Override
            public void onStartAsync(AsyncEvent event) {

            }
        });

        contextsMap.put(key, context);
    }

    /**
     * 通知监听当前配置文件的请求，并进行响应
     */
    public boolean notifySubscriber(EventType eventType, String env, String componentId, String namespace, String content) {
        String configKey = String.join("-", componentId, env, namespace);
        AsyncContext context = contextsMap.remove(configKey); // 获取并立即移除

        if (Objects.isNull(context)) {
            logger.debug("未找到监听者，跳过通知 [{}]", configKey);
            return false;
        }

        try {
            // 调用事件处理器，事件处理器内部会完成 context
            eventStrategyFactory.getEventStrategy(eventType)
                    .handleEvent(context, content, configKey);

        } catch (Exception e) {
            logger.error("通知订阅者失败 [{}]", configKey, e);
            // 发生异常时确保完成上下文
            safeCompleteContext(context);
            return false;
        }
        return true;
        // 移除 finally 块，避免重复 complete
    }

    /**
     * 安全地完成上下文
     */
    private void safeCompleteContext(AsyncContext context) {
        if (context == null) {
            return;
        }

        try {
            if (!context.getResponse().isCommitted()) {
                // 返回 500 错误
                ((HttpServletResponse) context.getResponse()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            context.complete();
        } catch (IllegalStateException e) {
            logger.debug("上下文已完成，跳过重复完成操作");
        } catch (Exception e) {
            logger.error("安全完成异步上下文失败", e);
        }
    }

    /**
     * 清理方法，用于应用关闭时
     */
    @PreDestroy
    public void destroy() {
        // 清理所有活跃的连接
        contextsMap.values().forEach(context -> {
            try {
                if (!context.getResponse().isCommitted()) {
                    ((HttpServletResponse) context.getResponse()).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                }
                context.complete();
            } catch (Exception e) {
                logger.warn("应用关闭时清理异步上下文失败", e);
            }
        });
        contextsMap.clear();
        timeoutChecker.shutdown();
    }



}
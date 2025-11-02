package com.old.silence.config.center.domain.service;


import jakarta.annotation.PreDestroy;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import com.old.silence.config.center.domain.repository.ConfigItemRepository;
import com.old.silence.config.center.domain.service.event.EventStrategyFactory;
import com.old.silence.config.center.enums.EventType;
import com.old.silence.json.JacksonMapper;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author moryzang
 */
@Service
public class LongPollingService {

    private final Map<String, AsyncContext> contextsMap = new ConcurrentHashMap<>();
    private final EventStrategyFactory eventStrategyFactory;
    private final ConfigItemRepository configItemRepository;
    private final ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1);

    public LongPollingService(EventStrategyFactory eventStrategyFactory,
                              ConfigItemRepository configItemRepository) {
        this.eventStrategyFactory = eventStrategyFactory;
        this.configItemRepository = configItemRepository;
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
                    // 返回304状态码表示未修改
                    ((HttpServletResponse) context.getResponse()).setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                } catch (Exception e) {
                    // 忽略异常
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
            public void onStartAsync(AsyncEvent event) {}
        });

        contextsMap.put(key, context);
    }

    /**
     * 通知监听当前配置文件的请求，并进行响应
     */
    public void notifySubscriber(EventType eventType, String env, String componentId, String namespace, String content) {
        String configKey = String.join("-", componentId, env, namespace);
        AsyncContext context = contextsMap.remove(configKey); // 获取并立即移除

        if (Objects.isNull(context)) {
            return;
        }

        try {
            // 调用事件处理器，事件处理器内部会完成 context
            eventStrategyFactory.getEventStrategy(eventType)
                    .handleEvent(context, content, configKey);

        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时确保完成上下文
            safeCompleteContext(context);
        }
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
                // 返回500错误
                ((HttpServletResponse) context.getResponse()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            context.complete();
        } catch (IllegalStateException e) {
            // 上下文已经完成，忽略
        } catch (Exception e) {
            e.printStackTrace();
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
                // 忽略异常
            }
        });
        contextsMap.clear();
        timeoutChecker.shutdown();
    }



}
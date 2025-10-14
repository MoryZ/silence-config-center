package com.old.silence.config.center.domain.service;


import jakarta.servlet.AsyncContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import com.old.silence.config.center.domain.repository.ConfigItemRepository;
import com.old.silence.config.center.domain.service.event.EventStrategyFactory;
import com.old.silence.config.center.enums.EventType;


import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
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
     *
     * @param namespace 命名空间
     * @param appId app
     * @param request 请求
     * @param response 响应
     */
    public void subscribeConfig(String group, String appId, String namespace,
                                HttpServletRequest request, HttpServletResponse response) {
        String key = String.join("-",appId,group , namespace);
        AsyncContext context = request.startAsync();
        // Set timeout, e.g., 10 seconds
        contextsMap.put(key, context);
        timeoutChecker.schedule(() -> {
            contextsMap.remove(key);
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            context.complete();
        }, 30000, TimeUnit.MILLISECONDS);

        /*
        //TODO 读多写少的场景 考虑使用一个读写锁来解决并发问题
        var optional = configInfoDao.findByappIdAndNamespaceAndGroup(appId, namespace, group,
                BigIdAndContentView.class);
        if (optional.isPresent()) {

            //如果有配置文件就直接返回 否则让他进行监听等待
            try {
                Result<String> data = Result.success(optional.get().getContent());
                context.getResponse().getWriter().write(JacksonMapper.getSharedInstance().toJson(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            context.complete();
        }*/
    }

    /**
     * 通知监听当前配置文件的请求，并进行响应
     *
     * @param namespace 命名空间
     * @param componentId app
     * @param content 变更内容
     */
    public void notifySubscriber(EventType eventType, String env, String componentId, String namespace, String content) {
        String key = String.join("-", componentId, env, namespace);
        AsyncContext context = contextsMap.get(key);
        if (Objects.isNull(context)){
            return;
        }
        try{
            eventStrategyFactory.getEventStrategy(eventType)
                    .handleEvent(context, content, key);
        }catch (Exception e){
            e.printStackTrace();
        } finally{
            contextsMap.remove(key);
        }

    }



}
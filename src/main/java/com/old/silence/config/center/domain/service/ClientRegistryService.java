package com.old.silence.config.center.domain.service;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.old.silence.config.center.domain.service.support.ClientInfo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author moryzang
 */
@Service
public class ClientRegistryService {


    private static final long HEARTBEAT_TIMEOUT_MS = 300_000; // 5分钟超时
    private static final Logger logger = LoggerFactory.getLogger(ClientRegistryService.class);
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
    // 存储结构: Map<配置Key, Set<客户端信息>>
    private final ConcurrentHashMap<String, Set<ClientInfo>> configListeners = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        cleaner.scheduleAtFixedRate(this::cleanInactiveClients,
                5, 5, TimeUnit.MINUTES);
    }

    // 注册客户端监听
    public synchronized void registerListener(String configKey, ClientInfo newClient) {

        Set<ClientInfo> clients = configListeners.computeIfAbsent(
                configKey,
                k -> ConcurrentHashMap.newKeySet()
        );

        // 移除同IP同端口的旧记录（处理重启场景）
        clients.removeIf(existing ->
                existing.isSameInstance(newClient)  // 基于IP、端口、进程ID等判断
        );

        clients.add(newClient);
        logger.debug("Registered client {} for config key {}", newClient, configKey);
    }

    public synchronized void unregisterClient(String clientId) {
        // 遍历所有配置的监听列表
        configListeners.forEach((configKey, clients) -> {
            clients.removeIf(client -> client.getClientId().equals(clientId));

            // 如果该配置已无监听者，移除空集合
            if (clients.isEmpty()) {
                configListeners.remove(configKey);
            }
        });

        logger.info("客户端[{}]已完全注销", clientId);
    }

    public synchronized void unregisterListener(String configKey, String clientId) {
        Set<ClientInfo> clients = configListeners.get(configKey);
        if (clients != null) {
            boolean removed = clients.removeIf(
                    client -> client.getClientId().equals(clientId)
            );

            if (removed) {
                logger.info("客户端[{}]已从配置[{}]注销", clientId, configKey);
            }

            // 清理空集合
            if (clients.isEmpty()) {
                configListeners.remove(configKey);
            }
        }
    }

    // 获取监听某配置的所有客户端IP
    public List<String> getListeningClientIps(String configKey) {
        Set<ClientInfo> clients = configListeners.getOrDefault(configKey, Collections.emptySet());
        return clients.stream()
                .filter(this::isActive)
                .map(ClientInfo::getIp)
                .collect(Collectors.toList());
    }

    // 心跳更新
    public void updateHeartbeat(String clientId) {
        configListeners.values().forEach(set ->
                set.stream()
                        .filter(info -> info.getClientId().equals(clientId))
                        .forEach(info -> info.setLastHeartbeat(System.currentTimeMillis()))
        );
    }

    private boolean isActive(ClientInfo info) {
        return System.currentTimeMillis() - info.getLastHeartbeat() < HEARTBEAT_TIMEOUT_MS;
    }

    private void cleanInactiveClients() {
        long threshold = Instant.now().minus(10, ChronoUnit.MINUTES).toEpochMilli();
        configListeners.forEach((configKey, clients) -> {
            clients.removeIf(clientInfo -> clientInfo.getLastHeartbeat() < threshold);
            if (clients.isEmpty()) {
                configListeners.remove(configKey, clients);
            }
        });
    }
}

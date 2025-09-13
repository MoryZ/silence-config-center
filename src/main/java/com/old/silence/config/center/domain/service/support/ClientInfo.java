package com.old.silence.config.center.domain.service.support;

import java.lang.management.ManagementFactory;
import java.util.Objects;

/**
 * @author moryzang
 */
public class ClientInfo {
    private String clientId;  // 客户端唯一ID
    private String ip;
    private String appName; // 新增：应用名称
    private String processId; // 新增：进程ID// 客户端IP
    private String configKey;// 监听的配置Key
    private long lastHeartbeat; // 最后心跳时间

    // 构造时自动获取进程ID
    public ClientInfo(String clientId, String ip, String configKey) {
        this.clientId = Objects.requireNonNull(clientId);
        this.ip = Objects.requireNonNull(ip);
        this.configKey = Objects.requireNonNull(configKey);
        this.processId = getRuntimeProcessId();
        this.appName = System.getProperty("spring.application.name", "unknown");
        this.lastHeartbeat = System.currentTimeMillis();
    }

    public ClientInfo() {

    }

    // 进程ID获取逻辑
    private static String getRuntimeProcessId() {
        try {
            String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
            return runtimeName.split("@")[0]; // 通常格式为PID@hostname
        } catch (Exception e) {
            return "unknown-pid";
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public boolean isSameInstance(ClientInfo other) {
        return this.ip.equals(other.getIp())
                && this.processId.equals(other.getProcessId());
    }
}

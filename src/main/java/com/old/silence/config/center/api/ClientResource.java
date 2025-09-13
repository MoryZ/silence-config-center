package com.old.silence.config.center.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.old.silence.config.center.domain.service.ClientRegistryService;
import com.old.silence.config.center.domain.service.support.ClientInfo;

/**
 * @author moryzang
 */
@RestController
@RequestMapping("/api/v1")
public class ClientResource {

    private final ClientRegistryService clientRegistryService;

    public ClientResource(ClientRegistryService clientRegistryService) {
        this.clientRegistryService = clientRegistryService;
    }

    /**
     * 注册监听接口
     *
     * @param configKey componentCode + env + filename 例如 A+CI-1+application.yml
     * @param clientId  客户id
     * @param ip        获取到的ip
     * @return 注册结果
     */
    @PostMapping("/client/register")
    public boolean register(
            @RequestParam String configKey,
            @RequestParam String clientId,
            @RequestParam String ip) {

        clientRegistryService.registerListener(
                configKey, new ClientInfo(clientId, ip, configKey)
        );
        return true;
    }

    /**
     * 取消注册监听接口
     *
     * @param configKey componentCode + env + filename 例如 A+CI-1+application.yml
     * @param clientId  客户id
     * @return 注册结果
     */
    @DeleteMapping("/client/unregister")
    public boolean unregister(
            @RequestParam String configKey,
            @RequestParam String clientId) {

        if (StringUtils.isNotBlank(configKey)) {
            // 注销特定配置的监听
            clientRegistryService.unregisterListener(configKey, clientId);
        } else {
            // 注销该客户端的全部监听
            clientRegistryService.unregisterClient(clientId);
        }
        return true;
    }

    // 心跳接口
    @PostMapping("/client/heartbeat")
    public void heartbeat(@RequestParam String clientId) {
        clientRegistryService.updateHeartbeat(clientId);
    }
}

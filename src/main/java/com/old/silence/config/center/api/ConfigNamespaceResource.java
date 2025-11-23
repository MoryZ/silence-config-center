package com.old.silence.config.center.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.old.silence.config.center.domain.service.ConfigNamespaceService;
import com.old.silence.config.center.dto.ConfigNamespaceCloneCommand;

/**
 * @author moryzang
 */
@RestController
@RequestMapping("/api/v1")
public class ConfigNamespaceResource {

    private final ConfigNamespaceService configNamespaceService;

    public ConfigNamespaceResource(ConfigNamespaceService configNamespaceService) {
        this.configNamespaceService = configNamespaceService;
    }

    @PostMapping("/configNamespaces/clone")
    public Boolean clone(@RequestBody ConfigNamespaceCloneCommand configNamespaceCloneCommand) {
        return configNamespaceService.clone(configNamespaceCloneCommand.getSourceEnvironmentId(), configNamespaceCloneCommand.getTargetEnvironmentId(),
                configNamespaceCloneCommand.getCloneMode());
    }
}

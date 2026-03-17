package com.old.silence.config.center.domain.service;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.old.silence.config.center.domain.model.ConfigAccessKeys;
import com.old.silence.config.center.domain.repository.ConfigAccessKeysRepository;
import com.old.silence.core.exception.ResourceNotFoundException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author moryzang
 */
@Component
public class DatabaseSecretManager {

    private final Cache<String, ConfigAccessKeys> accessKeyCache;

    private final ConfigAccessKeysRepository configAccessKeysRepository;

    public DatabaseSecretManager(ConfigAccessKeysRepository configAccessKeysRepository) {
        this.configAccessKeysRepository = configAccessKeysRepository;
        this.accessKeyCache = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    public ConfigAccessKeys getConfigAccessKeys(String accessKey) {
        return accessKeyCache.get(accessKey, key ->
                Optional.ofNullable(configAccessKeysRepository.findByAccessKey(accessKey)).orElseThrow(ResourceNotFoundException::new)
        );
    }
}
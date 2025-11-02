package com.old.silence.config.center.domain.service;

import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author moryzang
 */
@Component
public class NonceManager {

    private final Cache<String, Boolean> nonceCache;

    public NonceManager() {
        this.nonceCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
    }

    public boolean isNonceUsed(String nonce) {
        return nonceCache.getIfPresent(nonce) != null;
    }

    public void cacheNonce(String nonce) {
        nonceCache.put(nonce, true);
    }
}
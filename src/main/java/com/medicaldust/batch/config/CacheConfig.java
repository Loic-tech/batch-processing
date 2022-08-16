package com.medicaldust.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;


@RequiredArgsConstructor
@Configuration
@EnableScheduling
public class CacheConfig {

    private final CacheManager cacheManager;

    public void evictAllCaches() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Scheduled(fixedRate = 6000)
    public void evictAllCachesAtIntervals() {
        evictAllCaches();
    }
}

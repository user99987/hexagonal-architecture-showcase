package com.cp.ecommerce.adapter.persistence.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.cache.CacheManager;
import javax.cache.Caching;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for caching.
 */
@Configuration
@EnableCaching
public class PersistenceCacheConfiguration {

    @Bean
    @ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
    public org.springframework.cache.CacheManager cacheManager() {

        return new JCacheCacheManager(getCacheManager());
    }

    @Bean
    @ConditionalOnProperty(name = "cache.enabled", havingValue = "false")
    public org.springframework.cache.CacheManager noOpCacheManager() {

        return new NoOpCacheManager();
    }

    private CacheManager getCacheManager() {

        final CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        createCaches(cacheManager, createCacheConfigurations());
        return cacheManager;
    }

    private Map<String, CacheConfiguration<?, ?>> createCacheConfigurations() {

        final Map<String, CacheConfiguration<?, ?>> cacheConfigurations = new HashMap<>();
        Arrays.stream(CacheProperties.values())
                .forEach(
                        cache -> cacheConfigurations.put(
                                cache.getCacheName(),
                                CacheConfigurationBuilder
                                        .newCacheConfigurationBuilder(
                                                cache.getKeyType(),
                                                cache.getValueType(),
                                                ResourcePoolsBuilder.heap(cache.getMaxEntries()))
                                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(cache.getDuration()))
                                        .build()));
        return cacheConfigurations;
    }

    private void createCaches(
            final CacheManager cacheManager,
            final Map<String, CacheConfiguration<?, ?>> cacheConfigurations) {

        cacheConfigurations.forEach((name, configuration) -> {
            if (cacheManager.getCache(name) == null) {
                cacheManager.createCache(name, Eh107Configuration.fromEhcacheCacheConfiguration(configuration));
            }
        });
    }

}

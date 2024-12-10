package org.example.musk.plugin.lock.config;

import org.example.musk.plugin.lock.core.Lock;
import org.example.musk.plugin.lock.core.impl.RedisLock;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;


/**
 * Cache 配置类，基于 Redis 实现
 */
@AutoConfiguration
public class MuskPluginLockAutoConfiguration {


    @Bean
    @ConditionalOnBean(RedisCacheConfiguration.class)
    public Lock lock() {
        return new RedisLock();
    }

    @Bean
    public PluginLockSafeExecAspect pluginLockSafeExecAspect() {
        return new PluginLockSafeExecAspect();
    }

}

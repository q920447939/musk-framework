package org.example.musk.functions.cache.config;

import org.example.musk.functions.cache.aspect.CacheAspect;
import org.example.musk.functions.cache.core.CacheKeyBuilder;
import org.example.musk.functions.cache.core.CacheManager;
import org.example.musk.functions.cache.core.impl.DefaultCacheKeyBuilder;
import org.example.musk.functions.cache.core.impl.FunctionsCacheManagerImpl;
import org.example.musk.middleware.redis.config.MuskRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 缓存模块自动配置类
 * <p>
 * 自动配置缓存模块的核心组件
 *
 * @author musk-functions-cache
 */
@AutoConfiguration(after = MuskRedisAutoConfiguration.class)
@ComponentScan(basePackages = "org.example.musk.functions.cache")
@EnableAspectJAutoProxy
public class MuskFunctionsCacheAutoConfiguration {

    /**
     * 缓存键构建器
     */
    @Bean
    @ConditionalOnMissingBean(CacheKeyBuilder.class)
    public CacheKeyBuilder cacheKeyBuilder() {
        return new DefaultCacheKeyBuilder();
    }

    /**
     * 缓存管理器
     */
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager() {
        return new FunctionsCacheManagerImpl();
    }

    /**
     * 缓存注解处理切面
     */
    @Bean
    @ConditionalOnMissingBean(CacheAspect.class)
    public CacheAspect cacheAspect() {
        return new CacheAspect();
    }
}

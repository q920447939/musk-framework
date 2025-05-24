package org.example.musk.functions.cache.annotation;

import org.example.musk.functions.cache.model.CacheOptions;

import java.lang.annotation.*;

/**
 * 自定义缓存注解
 * <p>
 * 用于标记方法的返回值需要被缓存
 *
 * @author musk-functions-cache
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {

    /**
     * 缓存命名空间
     * <p>
     * 对应CacheNamespace枚举的名称
     */
    String namespace();

    /**
     * 缓存键
     * <p>
     * 支持SpEL表达式，如 "'user:' + #userId"
     */
    String key();

    /**
     * 缓存过期时间（秒）
     * <p>
     * 默认为3600秒（1小时）
     */
    long expireSeconds() default CacheOptions.DEFAULT_EXPIRE_SECONDS;

    /**
     * 是否缓存空值
     * <p>
     * 默认为false
     */
    boolean cacheNullValues() default false;


    /**
     * 是否自动添加租户ID前缀
     * <p>
     * 默认为true，会自动从ThreadLocalTenantContext获取租户ID并添加到缓存键前面
     */
    boolean autoTenantPrefix() default true;

    /**
     * 是否自动添加域ID前缀
     * <p>
     * 默认为true，会自动从ThreadLocalTenantContext获取域ID并添加到缓存键前面
     */
    boolean autoDomainPrefix() default true;
}

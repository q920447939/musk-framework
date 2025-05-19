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
     * 条件表达式
     * <p>
     * 只有当表达式结果为true时才进行缓存，支持SpEL表达式
     * 如果为空字符串，则表示无条件缓存
     */
    String condition() default "";
}

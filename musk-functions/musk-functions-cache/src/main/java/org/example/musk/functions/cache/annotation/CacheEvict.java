package org.example.musk.functions.cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存清除注解
 * <p>
 * 用于标记方法执行后需要清除缓存
 *
 * @author musk-functions-cache
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvict {

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
     * 当allEntries为true时，此属性可以为空
     */
    String key() default "";

    /**
     * 缓存键模式
     * <p>
     * 支持SpEL表达式，如 "'user:*'"
     * 用于批量删除缓存
     */
    String pattern() default "";


    /**
     * 是否在方法执行前清除缓存
     * <p>
     * 默认为false，即在方法执行后清除缓存
     */
    boolean beforeInvocation() default true;

    /**
     * 是否在方法执行后清除缓存
     * <p>
     * 默认为false，即在方法执行后清除缓存
     */
    boolean afterInvocation() default true;

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

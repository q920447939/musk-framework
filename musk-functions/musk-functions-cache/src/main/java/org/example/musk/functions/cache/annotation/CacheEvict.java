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
     * 是否清除命名空间下的所有缓存
     * <p>
     * 默认为false
     */
    boolean allEntries() default false;
    
    /**
     * 条件表达式
     * <p>
     * 只有当表达式结果为true时才清除缓存，支持SpEL表达式
     * 如果为空字符串，则表示无条件清除
     */
    String condition() default "";
    
    /**
     * 是否在方法执行前清除缓存
     * <p>
     * 默认为false，即在方法执行后清除缓存
     */
    boolean beforeInvocation() default false;
}

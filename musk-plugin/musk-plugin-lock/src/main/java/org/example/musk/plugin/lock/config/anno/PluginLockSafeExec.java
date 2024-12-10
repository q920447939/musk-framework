package org.example.musk.plugin.lock.config.anno;

import org.example.musk.plugin.lock.enums.LockGroupEnums;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 插件安全执行锁
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PluginLockSafeExec {
    LockGroupEnums group() default LockGroupEnums.GROUP_TENANT;

    String lockKey() default "";
    //单位 秒
    long ttl() default 60;
}

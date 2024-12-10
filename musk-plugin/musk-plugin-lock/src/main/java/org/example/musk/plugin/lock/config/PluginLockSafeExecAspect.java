package org.example.musk.plugin.lock.config;

import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.musk.plugin.lock.config.anno.PluginLockSafeExec;
import org.example.musk.plugin.lock.core.Lock;
import org.example.musk.plugin.lock.enums.LockGroupEnums;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

@Aspect
@Order(1)
public class PluginLockSafeExecAspect {


    @Resource
    private Lock lock;

    public PluginLockSafeExecAspect(){
        System.out.println(12);
    }


    //匹配方法和类上的注解
    @Pointcut("@within(org.example.musk.plugin.lock.config.anno.PluginLockSafeExec) || "
            + "@annotation(org.example.musk.plugin.lock.config.anno.PluginLockSafeExec)")
    public void anno() {
    }


    @Around("anno()")
    public Object around(ProceedingJoinPoint point) {

        // 获取目标方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 优先获取方法上的注解
        PluginLockSafeExec annotation = method.getAnnotation(PluginLockSafeExec.class);

        // 如果方法上没有，则获取类上的注解
        if (annotation == null) {
            Class<?> targetClass = point.getTarget().getClass();
            annotation = targetClass.getAnnotation(PluginLockSafeExec.class);
        }

        String lockKey;
        long ttl;
        LockGroupEnums group;
        if (annotation != null) {
            lockKey = annotation.lockKey();
            if ("".equals(lockKey)) {
                lockKey = getFullMethodName(point);
            }
            ttl = annotation.ttl();
            group = annotation.group();
        } else {
            throw new RuntimeException(" 未找到 @PluginLockSafeExec 注解");
        }
        return lock.safeExec("lock_key_auto:" + group.getCode() + ":"+lockKey, ttl, () -> {
            try {
                return point.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }


    private String getFullMethodName(ProceedingJoinPoint point) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获取类名
        String className = signature.getDeclaringType().getName();
        // 获取方法名
        String methodName = signature.getName();
        // 获取参数类型
        Class<?>[] parameterTypes = signature.getParameterTypes();
        // 拼接参数类型字符串
        StringBuilder params = new StringBuilder();
        for (Class<?> paramType : parameterTypes) {
            if (!params.isEmpty()) {
                params.append(",");
            }
            params.append(paramType.getSimpleName());
        }
        // 拼接全限定名
        return className + "." + methodName + "(" + params + ")";
    }

}

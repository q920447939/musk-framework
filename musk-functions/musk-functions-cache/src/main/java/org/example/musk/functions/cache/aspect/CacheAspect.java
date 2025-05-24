package org.example.musk.functions.cache.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.cache.core.CacheKeyBuilder;
import org.example.musk.functions.cache.core.CacheManager;
import org.example.musk.functions.cache.model.CacheNamespace;
import org.example.musk.functions.cache.model.CacheOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 缓存注解处理切面
 * <p>
 * 处理@Cacheable和@CacheEvict注解，实现方法缓存和缓存清除功能
 *
 * @author musk-functions-cache
 */
@Slf4j
@Aspect
@Component
public class CacheAspect {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 处理@Cacheable注解
     */
    @Around("@annotation(org.example.musk.functions.cache.annotation.Cacheable)")
    public Object handleCacheable(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Cacheable cacheable = method.getAnnotation(Cacheable.class);


        // 解析缓存键，并根据配置自动添加租户ID和域ID前缀
        String cacheKey = parseKeyWithPrefix(cacheable.key(), joinPoint, cacheable.autoTenantPrefix(), cacheable.autoDomainPrefix());
        CacheNamespace namespace = CacheNamespace.valueOf(cacheable.namespace());
        String fullCacheKey = cacheKeyBuilder.build(namespace, cacheKey);

        // 创建缓存选项
        CacheOptions options = CacheOptions.of(cacheable.expireSeconds(), cacheable.cacheNullValues());

        // 从缓存获取或计算
        return cacheManager.getOrCompute(fullCacheKey, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException("执行缓存方法异常", e);
            }
        }, options);
    }

    /**
     * 处理@CacheEvict注解
     */
    @Around("@annotation(org.example.musk.functions.cache.annotation.CacheEvict)")
    public Object handleCacheEvict(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CacheEvict cacheEvict = method.getAnnotation(CacheEvict.class);

        // 如果在方法执行前清除缓存
        if (cacheEvict.beforeInvocation()) {
            evictCache(cacheEvict, joinPoint);
        }
        Object r = joinPoint.proceed();
        if (cacheEvict.afterInvocation()) {
            evictCache(cacheEvict, joinPoint);
        }
        return r;
    }

    /**
     * 清除缓存
     */
    private void evictCache(CacheEvict cacheEvict, ProceedingJoinPoint joinPoint) {
        CacheNamespace namespace = CacheNamespace.valueOf(cacheEvict.namespace());

        // 按模式清除
        if (StringUtils.hasText(cacheEvict.pattern())) {
            String patternValue = parseKeyWithPrefix(cacheEvict.pattern(), joinPoint, cacheEvict.autoTenantPrefix(), cacheEvict.autoDomainPrefix());
            String pattern = cacheKeyBuilder.buildPattern(namespace, patternValue);
            cacheManager.removeByPattern(pattern);
            log.debug("按模式清除缓存: {}", pattern);
            return;
        }

        // 清除单个键
        if (StringUtils.hasText(cacheEvict.key())) {
            String cacheKey = parseKeyWithPrefix(cacheEvict.key(), joinPoint, cacheEvict.autoTenantPrefix(), cacheEvict.autoDomainPrefix());
            String fullCacheKey = cacheKeyBuilder.build(namespace, cacheKey);
            cacheManager.remove(fullCacheKey);
            log.debug("清除单个缓存: {}", fullCacheKey);
        }
    }

    /**
     * 解析SpEL表达式的缓存键
     */
    private String parseKey(String key, ProceedingJoinPoint joinPoint) {
        if (!key.contains("#")) {
            return key; // 不包含SpEL表达式，直接返回
        }

        EvaluationContext context = createEvaluationContext(joinPoint);
        Expression expression = expressionParser.parseExpression(key);
        Object value = expression.getValue(context);
        return value != null ? value.toString() : "";
    }

    /**
     * 解析SpEL表达式的缓存键，并根据配置自动添加租户ID和域ID前缀
     */
    private String parseKeyWithPrefix(String key, ProceedingJoinPoint joinPoint, boolean autoTenantPrefix, boolean autoDomainPrefix) {
        String parsedKey = parseKey(key, joinPoint);

        StringBuilder prefixedKey = new StringBuilder();

        // 添加租户ID前缀
        if (autoTenantPrefix) {
            Integer tenantId = ThreadLocalTenantContext.getTenantId();
            if (tenantId != null) {
                prefixedKey.append(tenantId).append(":");
            }
        }

        // 添加域ID前缀
        if (autoDomainPrefix) {
            Integer domainId = ThreadLocalTenantContext.getDomainId();
            if (domainId != null) {
                prefixedKey.append(domainId).append(":");
            }
        }

        // 添加原始键
        prefixedKey.append(parsedKey);

        return prefixedKey.toString();
    }

    /**
     * 创建SpEL表达式评估上下文
     */
    private EvaluationContext createEvaluationContext(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        StandardEvaluationContext context = new StandardEvaluationContext();

        // 添加方法参数到上下文
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }

        // 添加目标对象到上下文
        context.setVariable("target", joinPoint.getTarget());

        return context;
    }
}

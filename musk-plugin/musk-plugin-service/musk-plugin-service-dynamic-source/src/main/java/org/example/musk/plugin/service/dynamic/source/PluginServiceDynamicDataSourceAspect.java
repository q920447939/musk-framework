package org.example.musk.plugin.service.dynamic.source;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Order(1)
public class PluginServiceDynamicDataSourceAspect {

    private final PluginServiceDynamicDataSourceProperties properties;
    private final Map<String, Map<String, String>> moduleDataSourceMapping;

    public PluginServiceDynamicDataSourceAspect(PluginServiceDynamicDataSourceProperties properties) {
        this.properties = properties;
        this.moduleDataSourceMapping = properties.getDs().toMap();
    }

    //匹配方法和类上的注解
    @Pointcut("@within(org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource) || "
            + "@annotation(org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource)")
    public void anno() {
    }


    @Around("anno()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        // 获取目标方法
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 优先获取方法上的注解
        PluginDynamicSource dynamicSource = method.getAnnotation(PluginDynamicSource.class);

        // 如果方法上没有，则获取类上的注解
        if (dynamicSource == null) {
            Class<?> targetClass = point.getTarget().getClass();
            dynamicSource = targetClass.getAnnotation(PluginDynamicSource.class);
        }

        String group;
        String ds;
        if (dynamicSource != null) {
            group = dynamicSource.group();
            ds = dynamicSource.ds();
            // 使用获取到的 group 和 ds 值进行后续处理
        } else {
            throw new RuntimeException(" 未找到 @PluginDynamicSource 注解");
        }
        String dataSourceKey = determineDataSource(group,ds);

        if (StringUtils.hasText(dataSourceKey)) {
            DynamicDataSourceContextHolder.push(dataSourceKey);
        }
        try {
            return point.proceed();
        } finally {
            if (StringUtils.hasText(dataSourceKey)) {
                DynamicDataSourceContextHolder.poll();
            }
        }
    }

    private String determineDataSource(String group,String ds) {
        return moduleDataSourceMapping.get(group).get(ds);
    }
}

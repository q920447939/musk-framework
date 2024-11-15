package org.example.musk.auth.dynamic.source;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.util.Map;

@Aspect
@Order(1)
public class AuthDataSourceAspect {

    private final AuthDataSourceProperties properties;
    private final Map<String, String> moduleDataSourceMapping;

    public AuthDataSourceAspect(AuthDataSourceProperties properties) {
        this.properties = properties;
        this.moduleDataSourceMapping = properties.getDs().toMap();
    }

    @Around("execution(* org.example.musk.auth.service..*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String dataSourceKey = determineDataSource(point);

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

    private String determineDataSource(ProceedingJoinPoint point) {
        String className = point.getTarget().getClass().getName();

        // 根据包名判断模块
        for (Map.Entry<String, String> entry : moduleDataSourceMapping.entrySet()) {
            String module = entry.getKey();
            if (className.contains("." + module + ".")) {
                return entry.getValue();
            }
        }

        return null;
    }
}

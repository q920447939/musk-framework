package org.example.musk.framework.permission.web.config;

import jakarta.annotation.Resource;
import org.example.musk.framework.permission.web.interceptor.DomainInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限配置类
 *
 * @author musk-framework-permission
 */
@Configuration
public class PermissionConfiguration implements WebMvcConfigurer {

    @Resource
    private DomainInterceptor domainInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加领域拦截器
        registry.addInterceptor(domainInterceptor);
    }
}

package org.example.musk.auth.web.interceptor;

import jakarta.annotation.Resource;
import org.example.musk.framework.tenant.config.TenantConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private TenantInterceptor tenantInterceptor;

    @Resource
    private TenantConfig tenantConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (tenantConfig.getEnable()) {
            registry.addInterceptor(tenantInterceptor);
        }
    }
}

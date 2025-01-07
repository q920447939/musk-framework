package org.example.musk.plugin.web.upload.config;

import org.example.musk.plugin.web.upload.limit.member.DefaultTenantMemberUploadFileLimit;
import org.example.musk.plugin.web.upload.limit.member.TenantMemberUploadFileLimit;
import org.example.musk.plugin.web.upload.limit.tenant.DefaultTenantUploadFileLimit;
import org.example.musk.plugin.web.upload.limit.tenant.TenantUploadFileLimit;
import org.example.musk.plugin.web.upload.service.upload.ImageUploadServiceImpl;
import org.example.musk.plugin.web.upload.service.upload.UploadService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@EnableConfigurationProperties({UploadProperties.class,UploadFileLimitProperties.class})
public class MuskUpLoadConfiguration {

    @Bean
    @ConditionalOnMissingBean(UploadService.class)
    public UploadService imageUploadService(UploadProperties uploadProperties) {
        return new ImageUploadServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(TenantUploadFileLimit.class)
    public TenantUploadFileLimit tenantUploadFileLimit() {
        return new DefaultTenantUploadFileLimit();
    }
    @Bean
    @ConditionalOnMissingBean(TenantMemberUploadFileLimit.class)
    public TenantMemberUploadFileLimit tenantMemberUploadFileLimit() {
        return new DefaultTenantMemberUploadFileLimit();
    }
}

package org.example.musk.plugin.web.upload.helper;


import jakarta.annotation.Resource;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.plugin.web.upload.config.UploadProperties;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class UploadPathHelper {

    @Resource
    private UploadProperties uploadProperties;


    public String getTenantBasePath(){
        return uploadProperties.getBasePath() + ThreadLocalTenantContext.getTenantId() + File.separator;
    }

    public String getDomainFilePath(String filePath) {
        return (uploadProperties.getDomainProjectPath() + ThreadLocalTenantContext.getTenantId() + "/"+ filePath).replaceAll("\\\\", "/");
    }

}

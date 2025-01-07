package org.example.musk.plugin.web.upload.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "musk.plugin.web.upload.limit")
@Setter
@Getter
public class UploadFileLimitProperties {
    // 租户级别- 文件总个数
    private int tenantFileTotalNumber = Integer.MAX_VALUE;
    //租户级别- 文件总字节数
    private int tenantFileTotalBytes = Integer.MAX_VALUE;

    // 会员级别- 文件总个数
    private int tenantMemberFileTotalNumber = Integer.MAX_VALUE;
    // 会员级别- 文件总字节数
    private int tenantMemberFileTotalBytes = Integer.MAX_VALUE;
}

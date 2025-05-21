package org.example.musk.framework.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Set;

/**
 * 多域配置
 *
 * @author musk-framework-domain
 */
@ConfigurationProperties(prefix = "musk.framework.domain")
@Data
public class DomainConfig {

    /**
     * 域是否开启
     */
    private static final Boolean ENABLE_DEFAULT = true;

    /**
     * 是否开启
     */
    private Boolean enable = ENABLE_DEFAULT;

    /**
     * 需要忽略多域的表
     * 即默认所有表都开启多域的功能，所以记得添加对应的 domain_id 字段
     */
    private Set<String> ignoreTables = Collections.emptySet();

    /**
     * 读取配置的默认域ID
     */
    private Integer configDefaultDomainId;

    /**
     * 域ID列名
     */
    private String domainIdColumn = "domain_id";
}

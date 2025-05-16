package org.example.musk.enums.contextCache;


import lombok.Getter;

@Getter
public enum ContextRedisCacheTypeEnum {

    TENANT_PACKAGE("context-tenant_package","租户级别"),
    MEMBER("context-tenant_member","会员级别"),
    ;

    private String type;
    private String desc;

    ContextRedisCacheTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}

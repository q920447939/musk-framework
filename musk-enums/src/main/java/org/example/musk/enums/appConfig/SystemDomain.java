package org.example.musk.enums.appConfig;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年07月19日
 */
@Getter
public enum SystemDomain {
    APP((short) 1, "APP"),;

    private final Short domain;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param domain 类型
     * @param desc   desc
     */
    SystemDomain(Short domain, String desc) {
        this.domain = domain;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param domain 类型
     */
    public static SystemDomain getEnumsByType(short domain) {
        return Arrays.stream(SystemDomain.values()).filter(k -> k.getDomain() == domain).findFirst().orElseThrow(() -> new RuntimeException("未获取到枚举信息"));
    }

}

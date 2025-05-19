package org.example.musk.functions.navigation.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 导航所属域枚举
 *
 * @author musk-functions-navigation
 */
@Getter
public enum NavigationDomainEnum {

    APP(1, "APP端"),
    // 可以扩展其他域
    ;

    /**
     * 域值
     */
    private final Integer domain;

    /**
     * 域描述
     */
    private final String desc;

    NavigationDomainEnum(Integer domain, String desc) {
        this.domain = domain;
        this.desc = desc;
    }

    /**
     * 通过域值获取枚举
     *
     * @param domain 域值
     * @return 枚举
     */
    public static NavigationDomainEnum getByDomain(Integer domain) {
        return Arrays.stream(NavigationDomainEnum.values())
                .filter(e -> e.getDomain().equals(domain))
                .findFirst()
                .orElse(null);
    }
}

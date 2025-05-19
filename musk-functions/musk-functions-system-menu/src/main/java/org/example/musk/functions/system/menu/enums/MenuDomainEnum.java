package org.example.musk.functions.system.menu.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 菜单所属域枚举
 *
 * @author musk-functions-menu
 */
@Getter
public enum MenuDomainEnum {

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

    MenuDomainEnum(Integer domain, String desc) {
        this.domain = domain;
        this.desc = desc;
    }

    /**
     * 通过域值获取枚举
     *
     * @param domain 域值
     * @return 枚举
     */
    public static MenuDomainEnum getByDomain(Integer domain) {
        return Arrays.stream(MenuDomainEnum.values())
                .filter(e -> e.getDomain().equals(domain))
                .findFirst()
                .orElse(null);
    }
}

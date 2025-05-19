package org.example.musk.functions.navigation.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 导航平台类型枚举
 *
 * @author musk-functions-navigation
 */
@Getter
public enum NavigationPlatformEnum {

    APP(1, "APP端"),
    WEB(2, "WEB端"),
    // 可以扩展其他平台
    ;

    /**
     * 平台类型值
     */
    private final Integer type;

    /**
     * 平台描述
     */
    private final String desc;

    NavigationPlatformEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 通过类型值获取枚举
     *
     * @param type 类型值
     * @return 枚举
     */
    public static NavigationPlatformEnum getByType(Integer type) {
        return Arrays.stream(NavigationPlatformEnum.values())
                .filter(e -> e.getType().equals(type))
                .findFirst()
                .orElse(null);
    }
}

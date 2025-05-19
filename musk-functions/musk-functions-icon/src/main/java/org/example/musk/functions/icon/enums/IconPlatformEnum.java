package org.example.musk.functions.icon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图标平台类型枚举
 *
 * @author musk-functions-icon
 */
@Getter
@AllArgsConstructor
public enum IconPlatformEnum {

    /**
     * APP平台
     */
    APP(1, "APP平台"),

    /**
     * WEB平台
     */
    WEB(2, "WEB平台"),

    /**
     * 通用平台
     */
    COMMON(3, "通用平台");

    /**
     * 平台类型
     */
    private final Integer type;

    /**
     * 平台描述
     */
    private final String desc;

    /**
     * 根据类型获取枚举
     *
     * @param type 平台类型
     * @return 枚举
     */
    public static IconPlatformEnum getByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (IconPlatformEnum platformEnum : IconPlatformEnum.values()) {
            if (platformEnum.getType().equals(type)) {
                return platformEnum;
            }
        }
        return null;
    }
}

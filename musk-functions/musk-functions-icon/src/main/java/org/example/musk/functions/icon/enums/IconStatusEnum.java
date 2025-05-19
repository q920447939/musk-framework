package org.example.musk.functions.icon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图标状态枚举
 *
 * @author musk-functions-icon
 */
@Getter
@AllArgsConstructor
public enum IconStatusEnum {

    /**
     * 正常
     */
    NORMAL(0, "正常"),

    /**
     * 停用
     */
    DISABLED(1, "停用");

    /**
     * 状态码
     */
    private final Integer status;

    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 根据状态码获取枚举
     *
     * @param status 状态码
     * @return 枚举
     */
    public static IconStatusEnum getByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        for (IconStatusEnum statusEnum : IconStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }
}

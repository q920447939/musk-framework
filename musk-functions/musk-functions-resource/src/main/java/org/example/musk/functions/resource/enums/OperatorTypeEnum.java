package org.example.musk.functions.resource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作人类型枚举
 *
 * @author musk-functions-resource
 */
@Getter
@AllArgsConstructor
public enum OperatorTypeEnum {

    /**
     * 系统用户
     */
    SYSTEM_USER(1, "系统用户"),

    /**
     * 会员
     */
    MEMBER(2, "会员");

    /**
     * 类型编码
     */
    private final Integer code;

    /**
     * 类型描述
     */
    private final String description;

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static OperatorTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OperatorTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}

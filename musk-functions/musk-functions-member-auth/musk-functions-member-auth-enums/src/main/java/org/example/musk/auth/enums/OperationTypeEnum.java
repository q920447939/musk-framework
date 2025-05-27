package org.example.musk.auth.enums;

import lombok.Getter;

/**
 * 操作类型枚举
 * 
 * @author musk
 */
@Getter
public enum OperationTypeEnum {

    /**
     * 绑定
     */
    BIND("BIND", "绑定"),

    /**
     * 解绑
     */
    UNBIND("UNBIND", "解绑"),

    /**
     * 更换
     */
    CHANGE("CHANGE", "更换");

    /**
     * 操作编码
     */
    private final String code;

    /**
     * 操作描述
     */
    private final String desc;

    OperationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 操作编码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static OperationTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (OperationTypeEnum operation : values()) {
            if (operation.getCode().equals(code)) {
                return operation;
            }
        }
        return null;
    }
}

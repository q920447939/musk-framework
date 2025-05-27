package org.example.musk.auth.enums;

import lombok.Getter;

/**
 * 安全操作类型枚举
 * 
 * @author musk
 */
@Getter
public enum SecurityOperationEnum {

    /**
     * 修改密码
     */
    CHANGE_PASSWORD("CHANGE_PASSWORD", "修改密码"),

    /**
     * 重置密码
     */
    RESET_PASSWORD("RESET_PASSWORD", "重置密码"),

    /**
     * 忘记密码
     */
    FORGOT_PASSWORD("FORGOT_PASSWORD", "忘记密码"),

    /**
     * 绑定邮箱
     */
    BIND_EMAIL("BIND_EMAIL", "绑定邮箱"),

    /**
     * 解绑邮箱
     */
    UNBIND_EMAIL("UNBIND_EMAIL", "解绑邮箱"),

    /**
     * 更换邮箱
     */
    CHANGE_EMAIL("CHANGE_EMAIL", "更换邮箱"),

    /**
     * 绑定手机号
     */
    BIND_PHONE("BIND_PHONE", "绑定手机号"),

    /**
     * 解绑手机号
     */
    UNBIND_PHONE("UNBIND_PHONE", "解绑手机号"),

    /**
     * 更换手机号
     */
    CHANGE_PHONE("CHANGE_PHONE", "更换手机号"),

    /**
     * 修改基础信息
     */
    UPDATE_BASIC_INFO("UPDATE_BASIC_INFO", "修改基础信息"),

    /**
     * 查看安全日志
     */
    VIEW_SECURITY_LOG("VIEW_SECURITY_LOG", "查看安全日志");

    /**
     * 操作编码
     */
    private final String code;

    /**
     * 操作描述
     */
    private final String desc;

    SecurityOperationEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 操作编码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static SecurityOperationEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (SecurityOperationEnum operation : values()) {
            if (operation.getCode().equals(code)) {
                return operation;
            }
        }
        return null;
    }

    /**
     * 判断是否为密码相关操作
     *
     * @return true-密码相关，false-非密码相关
     */
    public boolean isPasswordRelated() {
        return this == CHANGE_PASSWORD || this == RESET_PASSWORD || this == FORGOT_PASSWORD;
    }

    /**
     * 判断是否为联系方式相关操作
     *
     * @return true-联系方式相关，false-非联系方式相关
     */
    public boolean isContactRelated() {
        return this == BIND_EMAIL || this == UNBIND_EMAIL || this == CHANGE_EMAIL ||
               this == BIND_PHONE || this == UNBIND_PHONE || this == CHANGE_PHONE;
    }

    /**
     * 判断是否为高敏感度操作
     *
     * @return true-高敏感度，false-非高敏感度
     */
    public boolean isHighSensitive() {
        return isPasswordRelated() || isContactRelated();
    }
}

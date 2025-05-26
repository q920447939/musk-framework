package org.example.musk.auth.enums.auth;

import lombok.Getter;

/**
 * 认证类型枚举
 * 
 * @author musk
 */
@Getter
public enum AuthTypeEnum {

    /**
     * 用户名密码登录
     */
    USERNAME_PASSWORD(1, "用户名密码登录"),

    /**
     * 邮箱验证码登录
     */
    EMAIL_CODE(2, "邮箱验证码登录"),

    /**
     * 短信验证码登录
     */
    SMS_CODE(3, "短信验证码登录"),

    /**
     * 微信登录
     */
    WECHAT(4, "微信登录"),

    /**
     * QQ登录
     */
    QQ(5, "QQ登录"),

    /**
     * 支付宝登录
     */
    ALIPAY(6, "支付宝登录"),

    /**
     * GitHub登录
     */
    GITHUB(7, "GitHub登录"),

    /**
     * Google登录
     */
    GOOGLE(8, "Google登录");

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型描述
     */
    private final String desc;

    AuthTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 类型值
     * @return 对应的枚举，如果不存在则返回null
     */
    public static AuthTypeEnum fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AuthTypeEnum authType : values()) {
            if (authType.getValue().equals(value)) {
                return authType;
            }
        }
        return null;
    }

    /**
     * 判断是否为第三方登录类型
     *
     * @return true-第三方登录，false-非第三方登录
     */
    public boolean isThirdParty() {
        return this == WECHAT || this == QQ || this == ALIPAY || 
               this == GITHUB || this == GOOGLE;
    }

    /**
     * 判断是否为验证码登录类型
     *
     * @return true-验证码登录，false-非验证码登录
     */
    public boolean isVerificationCode() {
        return this == EMAIL_CODE || this == SMS_CODE;
    }
}

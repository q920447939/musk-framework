package org.example.musk.auth.enums.auth;

import lombok.Getter;

/**
 * 认证渠道类型枚举
 * 
 * @author musk
 */
@Getter
public enum AuthChannelTypeEnum {

    /**
     * 用户名密码
     */
    USERNAME("USERNAME", "用户名密码"),

    /**
     * 邮箱
     */
    EMAIL("EMAIL", "邮箱"),

    /**
     * 手机号
     */
    PHONE("PHONE", "手机号"),

    /**
     * 微信
     */
    WECHAT("WECHAT", "微信"),

    /**
     * QQ
     */
    QQ("QQ", "QQ"),

    /**
     * 支付宝
     */
    ALIPAY("ALIPAY", "支付宝"),

    /**
     * GitHub
     */
    GITHUB("GITHUB", "GitHub"),

    /**
     * Google
     */
    GOOGLE("GOOGLE", "Google");

    /**
     * 渠道编码
     */
    private final String code;

    /**
     * 渠道描述
     */
    private final String desc;

    AuthChannelTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 渠道编码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static AuthChannelTypeEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (AuthChannelTypeEnum channelType : values()) {
            if (channelType.getCode().equals(code)) {
                return channelType;
            }
        }
        return null;
    }

    /**
     * 判断是否为第三方渠道
     *
     * @return true-第三方渠道，false-非第三方渠道
     */
    public boolean isThirdParty() {
        return this == WECHAT || this == QQ || this == ALIPAY || 
               this == GITHUB || this == GOOGLE;
    }

    /**
     * 判断是否为验证码渠道
     *
     * @return true-验证码渠道，false-非验证码渠道
     */
    public boolean isVerificationCode() {
        return this == EMAIL || this == PHONE;
    }

    /**
     * 判断是否需要密码验证
     *
     * @return true-需要密码，false-不需要密码
     */
    public boolean requiresPassword() {
        return this == USERNAME;
    }
}

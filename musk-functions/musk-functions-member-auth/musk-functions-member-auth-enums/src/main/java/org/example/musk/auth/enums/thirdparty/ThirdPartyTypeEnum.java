package org.example.musk.auth.enums.thirdparty;

import lombok.Getter;

/**
 * 第三方登录类型枚举
 * 
 * @author musk
 */
@Getter
public enum ThirdPartyTypeEnum {

    /**
     * 微信
     */
    WECHAT("wechat", "微信", "微信开放平台"),

    /**
     * QQ
     */
    QQ("qq", "QQ", "QQ互联"),

    /**
     * 支付宝
     */
    ALIPAY("alipay", "支付宝", "支付宝开放平台"),

    /**
     * GitHub
     */
    GITHUB("github", "GitHub", "GitHub OAuth"),

    /**
     * Google
     */
    GOOGLE("google", "Google", "Google OAuth 2.0");

    /**
     * 第三方平台标识（对应JustAuth的source）
     */
    private final String source;

    /**
     * 平台名称
     */
    private final String name;

    /**
     * 平台描述
     */
    private final String desc;

    ThirdPartyTypeEnum(String source, String name, String desc) {
        this.source = source;
        this.name = name;
        this.desc = desc;
    }

    /**
     * 根据source获取枚举
     *
     * @param source 第三方平台标识
     * @return 对应的枚举，如果不存在则返回null
     */
    public static ThirdPartyTypeEnum fromSource(String source) {
        if (source == null) {
            return null;
        }
        for (ThirdPartyTypeEnum thirdPartyType : values()) {
            if (thirdPartyType.getSource().equals(source)) {
                return thirdPartyType;
            }
        }
        return null;
    }

    /**
     * 判断是否为国内平台
     *
     * @return true-国内平台，false-国外平台
     */
    public boolean isDomestic() {
        return this == WECHAT || this == QQ || this == ALIPAY;
    }

    /**
     * 判断是否为国外平台
     *
     * @return true-国外平台，false-国内平台
     */
    public boolean isForeign() {
        return !isDomestic();
    }
}

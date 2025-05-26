package org.example.musk.auth.enums.code;

import lombok.Getter;

/**
 * 验证码渠道枚举
 * 
 * @author musk
 */
@Getter
public enum CodeChannelEnum {

    /**
     * 邮箱
     */
    EMAIL("EMAIL", "邮箱"),

    /**
     * 短信
     */
    SMS("SMS", "短信");

    /**
     * 渠道编码
     */
    private final String code;

    /**
     * 渠道描述
     */
    private final String desc;

    CodeChannelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 渠道编码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static CodeChannelEnum fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (CodeChannelEnum channel : values()) {
            if (channel.getCode().equals(code)) {
                return channel;
            }
        }
        return null;
    }
}

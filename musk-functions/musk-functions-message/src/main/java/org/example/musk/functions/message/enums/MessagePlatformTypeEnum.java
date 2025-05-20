package org.example.musk.functions.message.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 消息平台类型枚举
 *
 * @author musk-functions-message
 */
@Getter
public enum MessagePlatformTypeEnum {

    APP(1, "APP"),
    WEB(2, "WEB"),
    ALL(3, "全平台");

    /**
     * 平台类型值
     */
    private final Integer type;

    /**
     * 平台类型描述
     */
    private final String desc;

    MessagePlatformTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据平台类型值获取枚举
     *
     * @param type 平台类型值
     * @return 枚举实例
     */
    public static MessagePlatformTypeEnum getByType(Integer type) {
        return Arrays.stream(MessagePlatformTypeEnum.values())
                .filter(e -> e.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的消息平台类型: " + type));
    }
}

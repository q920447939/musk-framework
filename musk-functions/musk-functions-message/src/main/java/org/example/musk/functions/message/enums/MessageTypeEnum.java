package org.example.musk.functions.message.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 消息类型枚举
 *
 * @author musk-functions-message
 */
@Getter
public enum MessageTypeEnum {

    TEXT(1, "文本消息"),
    IMAGE_TEXT(2, "图文消息"),
    SYSTEM_NOTICE(3, "系统通知"),
    UPDATE_NOTICE(4, "更新通知");

    /**
     * 类型值
     */
    private final Integer type;

    /**
     * 类型描述
     */
    private final String desc;

    MessageTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据类型值获取枚举
     *
     * @param type 类型值
     * @return 枚举实例
     */
    public static MessageTypeEnum getByType(Integer type) {
        return Arrays.stream(MessageTypeEnum.values())
                .filter(e -> e.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的消息类型: " + type));
    }
}

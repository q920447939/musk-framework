package org.example.musk.functions.message.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 消息操作类型枚举
 *
 * @author musk-functions-message
 */
@Getter
public enum MessageActionTypeEnum {

    NONE(0, "无操作"),
    LINK(1, "跳转链接"),
    APP(2, "打开应用"),
    DOWNLOAD(3, "下载更新");

    /**
     * 操作类型值
     */
    private final Integer type;

    /**
     * 操作类型描述
     */
    private final String desc;

    MessageActionTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据操作类型值获取枚举
     *
     * @param type 操作类型值
     * @return 枚举实例
     */
    public static MessageActionTypeEnum getByType(Integer type) {
        return Arrays.stream(MessageActionTypeEnum.values())
                .filter(e -> e.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的消息操作类型: " + type));
    }
}

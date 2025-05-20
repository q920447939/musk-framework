package org.example.musk.functions.message.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 消息状态枚举
 *
 * @author musk-functions-message
 */
@Getter
public enum MessageStatusEnum {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    EXPIRED(2, "已过期");

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态描述
     */
    private final String desc;

    MessageStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 根据状态值获取枚举
     *
     * @param status 状态值
     * @return 枚举实例
     */
    public static MessageStatusEnum getByStatus(Integer status) {
        return Arrays.stream(MessageStatusEnum.values())
                .filter(e -> e.getStatus().equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的消息状态: " + status));
    }
}

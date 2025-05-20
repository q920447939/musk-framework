package org.example.musk.functions.message.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 消息发送状态枚举
 *
 * @author musk-functions-message
 */
@Getter
public enum MessageSendStatusEnum {

    PENDING(0, "待发送"),
    SENDING(1, "发送中"),
    SUCCESS(2, "发送成功"),
    FAILED(3, "发送失败");

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 状态描述
     */
    private final String desc;

    MessageSendStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 根据状态值获取枚举
     *
     * @param status 状态值
     * @return 枚举实例
     */
    public static MessageSendStatusEnum getByStatus(Integer status) {
        return Arrays.stream(MessageSendStatusEnum.values())
                .filter(e -> e.getStatus().equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的消息发送状态: " + status));
    }
}

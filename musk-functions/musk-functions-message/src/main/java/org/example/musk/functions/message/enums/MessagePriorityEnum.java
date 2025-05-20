package org.example.musk.functions.message.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 消息优先级枚举
 *
 * @author musk-functions-message
 */
@Getter
public enum MessagePriorityEnum {

    NORMAL(0, "普通"),
    IMPORTANT(1, "重要"),
    URGENT(2, "紧急");

    /**
     * 优先级值
     */
    private final Integer priority;

    /**
     * 优先级描述
     */
    private final String desc;

    MessagePriorityEnum(Integer priority, String desc) {
        this.priority = priority;
        this.desc = desc;
    }

    /**
     * 根据优先级值获取枚举
     *
     * @param priority 优先级值
     * @return 枚举实例
     */
    public static MessagePriorityEnum getByPriority(Integer priority) {
        return Arrays.stream(MessagePriorityEnum.values())
                .filter(e -> e.getPriority().equals(priority))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的消息优先级: " + priority));
    }
}

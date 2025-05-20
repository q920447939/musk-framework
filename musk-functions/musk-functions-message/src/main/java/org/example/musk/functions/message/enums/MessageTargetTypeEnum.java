package org.example.musk.functions.message.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 消息目标类型枚举
 *
 * @author musk-functions-message
 */
@Getter
public enum MessageTargetTypeEnum {

    USER(1, "单个用户"),
    USER_GROUP(2, "用户组"),
    ALL_USERS(3, "全部用户");

    /**
     * 类型值
     */
    private final Integer type;

    /**
     * 类型描述
     */
    private final String desc;

    MessageTargetTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据类型值获取枚举
     *
     * @param type 类型值
     * @return 枚举实例
     */
    public static MessageTargetTypeEnum getByType(Integer type) {
        return Arrays.stream(MessageTargetTypeEnum.values())
                .filter(e -> e.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的消息目标类型: " + type));
    }
}

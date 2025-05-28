package org.example.musk.functions.invitation.dao.enums;

import lombok.Getter;

/**
 * 邀请触发事件枚举
 *
 * @author musk-functions-member-invitation
 */
@Getter
public enum InvitationTriggerEventEnum {

    /**
     * 注册
     */
    REGISTER("REGISTER", "注册"),

    /**
     * 首单
     */
    FIRST_ORDER("FIRST_ORDER", "首单"),

    /**
     * 消费
     */
    CONSUMPTION("CONSUMPTION", "消费");

    private final String code;
    private final String description;

    InvitationTriggerEventEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static InvitationTriggerEventEnum fromCode(String code) {
        for (InvitationTriggerEventEnum eventEnum : values()) {
            if (eventEnum.getCode().equals(code)) {
                return eventEnum;
            }
        }
        return null;
    }

}

package org.example.musk.functions.invitation.dao.enums;

import lombok.Getter;

/**
 * 邀请码类型枚举
 *
 * @author musk-functions-member-invitation
 */
@Getter
public enum InvitationCodeTypeEnum {

    /**
     * 个人邀请码
     */
    PERSONAL(1, "个人邀请码"),

    /**
     * 活动邀请码
     */
    ACTIVITY(2, "活动邀请码");

    private final Integer code;
    private final String description;

    InvitationCodeTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static InvitationCodeTypeEnum fromCode(Integer code) {
        for (InvitationCodeTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

}

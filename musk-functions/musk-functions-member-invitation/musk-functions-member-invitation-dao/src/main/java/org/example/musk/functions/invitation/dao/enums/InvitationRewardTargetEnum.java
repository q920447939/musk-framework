package org.example.musk.functions.invitation.dao.enums;

import lombok.Getter;

/**
 * 邀请奖励对象枚举
 *
 * @author musk-functions-member-invitation
 */
@Getter
public enum InvitationRewardTargetEnum {

    /**
     * 邀请人
     */
    INVITER(1, "邀请人"),

    /**
     * 被邀请人
     */
    INVITEE(2, "被邀请人"),

    /**
     * 双方
     */
    BOTH(3, "双方");

    private final Integer code;
    private final String description;

    InvitationRewardTargetEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static InvitationRewardTargetEnum fromCode(Integer code) {
        for (InvitationRewardTargetEnum targetEnum : values()) {
            if (targetEnum.getCode().equals(code)) {
                return targetEnum;
            }
        }
        return null;
    }

}

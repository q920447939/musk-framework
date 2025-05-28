package org.example.musk.functions.invitation.dao.enums;

import lombok.Getter;

/**
 * 邀请奖励发放状态枚举
 *
 * @author musk-functions-member-invitation
 */
@Getter
public enum InvitationRewardStatusEnum {

    /**
     * 待发放
     */
    PENDING(1, "待发放"),

    /**
     * 已发放
     */
    GRANTED(2, "已发放"),

    /**
     * 发放失败
     */
    FAILED(3, "发放失败"),

    /**
     * 已取消
     */
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String description;

    InvitationRewardStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static InvitationRewardStatusEnum fromCode(Integer code) {
        for (InvitationRewardStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }

}

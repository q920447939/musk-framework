package org.example.musk.functions.invitation.dao.enums;

import lombok.Getter;

/**
 * 邀请奖励类型枚举
 *
 * @author musk-functions-member-invitation
 */
@Getter
public enum InvitationRewardTypeEnum {

    /**
     * 积分
     */
    POINTS("POINTS", "积分"),

    /**
     * 成长值
     */
    GROWTH("GROWTH", "成长值"),

    /**
     * 优惠券
     */
    COUPON("COUPON", "优惠券"),

    /**
     * 会员天数
     */
    MEMBER_DAYS("MEMBER_DAYS", "会员天数");

    private final String code;
    private final String description;

    InvitationRewardTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static InvitationRewardTypeEnum fromCode(String code) {
        for (InvitationRewardTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

}

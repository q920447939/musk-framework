package org.example.musk.functions.invitation.dao.enums;

import lombok.Getter;

/**
 * 邀请码状态枚举
 *
 * @author musk-functions-member-invitation
 */
@Getter
public enum InvitationCodeStatusEnum {

    /**
     * 有效
     */
    VALID(1, "有效"),

    /**
     * 失效
     */
    INVALID(2, "失效"),

    /**
     * 禁用
     */
    DISABLED(3, "禁用");

    private final Integer code;
    private final String description;

    InvitationCodeStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static InvitationCodeStatusEnum fromCode(Integer code) {
        for (InvitationCodeStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }

}

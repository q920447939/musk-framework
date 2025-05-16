package org.example.musk.enums.member;

/**
 * 会员实名认证申请状态
 */
public enum MemberRealNameAuthStatusEnums {
    APPLYING(1, "申请中"),
    AUTH_SUCCESS(2, "认证成功"),
    AUTH_FAIL(3, "认证失败"),
    ;

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String desc;

    MemberRealNameAuthStatusEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }




    public static MemberRealNameAuthStatusEnums getByValue(Integer status) {
        for (MemberRealNameAuthStatusEnums enums : MemberRealNameAuthStatusEnums.values()) {
            if (enums.getStatus().equals(status)) {
                return enums;
            }
        }
        return null;
    }
}

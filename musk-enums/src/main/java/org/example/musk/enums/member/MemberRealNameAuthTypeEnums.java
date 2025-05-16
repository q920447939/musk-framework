package org.example.musk.enums.member;

/**
 * 会员实名认证国家
 */
public enum MemberRealNameAuthTypeEnums {
    SFZ(1, "身份证"),
    ;

    /**
     * 状态值
     */
    private final Integer type;
    /**
     * 状态名
     */
    private final String desc;

    MemberRealNameAuthTypeEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static MemberRealNameAuthTypeEnums getByValue(Integer type) {
        for (MemberRealNameAuthTypeEnums enums : MemberRealNameAuthTypeEnums.values()) {
            if (enums.getType().equals(type)) {
                return enums;
            }
        }
        return null;
    }


}

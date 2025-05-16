package org.example.musk.enums.member;

/**
 * 会员可用金额历史，可用金额 增加或减少
 */
public enum MemberAmountHistoryAmountAddOrSubEnums {


    ADD((byte)1, "增加"),

    SUB((byte)2, "减少");

    /**
     * 增加或减少
     */
    private final Byte type;
    /**
     * 描述
     */
    private final String desc;

    MemberAmountHistoryAmountAddOrSubEnums(Byte type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Byte getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}

package org.example.musk.enums.member;

/**
 * 会员 拥有的代金券 使用状态
 */
public enum MemberVoucherUseStatusEnums {

    /**
     * 未使用
     */
    UN_USE(1, "未使用"),
    /**
     * 已使用
     */
    USED(2, "已使用");

    /**
     * 状态值
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String desc;

    MemberVoucherUseStatusEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}

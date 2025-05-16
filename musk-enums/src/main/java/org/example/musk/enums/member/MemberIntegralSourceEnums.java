package org.example.musk.enums.member;

/**
 * 积分来源类型
 *
 * @author
 * @date 2024/07/08
 */
public enum MemberIntegralSourceEnums {

    INVEST_PROJECT_ADD((short)1, "投资奖励"),
    DRAW_ADD((short)2, "抽奖奖励"),
    INVEST_PLAN_CANCEL((short)3, "取消投资扣减"),
    SIGN_IN_BASE_REWARD_BY_INTEGRAL((short)4, "签到基础奖励-积分奖励"),
    SIGN_IN_EXTRA_REWARD_BY_INTEGRAL((short)5, "签到额外奖励-积分奖励"),
    INTEGRAL_MALL_APPLY((short)6, "积分商城兑换"),
    INTEGRAL_MALL_APPLY_CANCEL((short)7, "积分商城兑换失败返还"),
    ;

    /**
     * 状态值
     */
    private final Short sourceType;
    /**
     * 状态名
     */
    private final String desc;


    MemberIntegralSourceEnums(Short sourceType, String desc) {
        this.sourceType = sourceType;
        this.desc = desc;
    }

    /**
     * 获得源类型
     *
     * @return {@link Short }
     */
    public Short getSourceType() {
        return sourceType;
    }

    /**
     * 获得desc
     *
     * @return {@link String }
     */
    public String getDesc() {
        return desc;
    }
}

package org.example.musk.enums.member;

/**
 * 会员可用金额历史，来源类型
 *
 * @author
 * @date 2024/07/08
 */
public enum MemberAmountHistorySourceEnums {


    PROJECT_INVEST_SUB((short)1, "项目投资扣减"),

    PROJECT_DAILY_EARN((short)2, "项目每日收益"),

    PROJECT_INCOME_END_RETURN((short)3, "项目收益到期返回"),
    TRANSFER_OUT((short)4, "转账支出"),
    TRANSFER_IN((short)5, "转账收入"),
    BROKERAGE_IN((short)6, "分佣奖励"),
    INVEST_PROJECT_CANCEL_DEDUCT((short)7, "投资取消扣减"),

    SIGN_IN_BASE_REWARD_BY_USDT((short)8, "签到基础奖励-USDT奖励"),
    SIGN_IN_EXTRA_REWARD_BY_USDT((short)9, "签到额外奖励-USDT奖励"),


    TEAM_LEADER_REWARD((short)10, "团队奖励"),
    RECHARGE_INCOME((short)11, "充值"),
    CASH_OUT((short)120, "提现扣减"),
    CASH_IN((short)121, "提现失败返还"),


    END((short)999, "未知");

    /**
     * 状态值
     */
    private final Short sourceType;
    /**
     * 状态名
     */
    private final String desc;

    /**
     * 会员量历史源枚举
     *
     * @param sourceType 源类型
     * @param desc       desc
     */
    MemberAmountHistorySourceEnums(Short sourceType, String desc) {
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

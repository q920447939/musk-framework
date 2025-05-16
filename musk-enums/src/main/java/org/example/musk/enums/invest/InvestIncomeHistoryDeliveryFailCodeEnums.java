package org.example.musk.enums.invest;

import lombok.Getter;

/**
 * @Description: 投资收益历史失败发放状态 对应的错误码
 * @date 2024年06月28日
 * 三位数字
 * 百位 十位 个位 都有含义
 */

@Getter
public enum InvestIncomeHistoryDeliveryFailCodeEnums {

    INCOME_TIME_ILLEGAL(101,"收益时间区间异常"),
    PROJECT_IS_NULL(102,"获取项目失败"),
    MEMBER_IS_NULL(103,"获取会员信息失败"),
    INCOME_POOL_IS_NULL(104,"获取收益池数据失败"),
    ;

    private Integer failCode;
    private String reason;

    InvestIncomeHistoryDeliveryFailCodeEnums(Integer failCode, String reason) {
        this.failCode = failCode;
        this.reason = reason;
    }

}

package org.example.musk.enums.invest;

import lombok.Getter;

/**
 * @Description: 投资收益历史发放状态
 * @date 2024年06月28日
 */

@Getter
public enum InvestIncomeHistoryDeliveryStatus {

    SUCC((short)1,"成功"),
    FAIL((short)2,"失败"),
    ;

    private Short status;
    private String desc;

    InvestIncomeHistoryDeliveryStatus(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}

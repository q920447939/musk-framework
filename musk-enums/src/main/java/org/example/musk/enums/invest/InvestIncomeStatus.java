package org.example.musk.enums.invest;

import lombok.Getter;

/**
 * @Description: 投资收益状态
 * @date 2024年06月28日
 */

@Getter
public enum InvestIncomeStatus {

    STOP((short)1,"暂停"),
    RUNNING((short)2,"运行中"),
    END((short)3,"结束"),
    ;

    private Short status;
    private String desc;

    InvestIncomeStatus(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}

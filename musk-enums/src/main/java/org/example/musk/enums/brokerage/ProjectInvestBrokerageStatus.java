package org.example.musk.enums.brokerage;

import lombok.Getter;

@Getter
public enum ProjectInvestBrokerageStatus {

    WAITING((short)1,"待处理"),
    RUNNING((short)2,"进行中"),
    END((short)3,"已结束")
    ;

    private Short status;
    private String desc;

    ProjectInvestBrokerageStatus(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}

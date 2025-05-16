package org.example.musk.enums.brokerage;

import lombok.Getter;

@Getter
public enum ProjectInvestBrokerageLevelEnums {

    PAREN((short)1,"父"),
    GRAND_PA((short)2,"祖父"),
    GREAT_GRAND_PA((short)3,"曾祖父"),
    ;

    private Short status;
    private String desc;

    ProjectInvestBrokerageLevelEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}

package org.example.musk.enums.projectAllowRelation;


import lombok.Getter;


/**
 * app项目允许使用劵关联
 * 使用类型(1:代金券)
 */
@Getter
public enum ProjectVoucherRelationAllowUseTypeEnums {
    VOUCHER(1,"代金券"),
    BROKERAGE(2,"分佣"),
    ;

    private Integer type;
    private String desc;

    ProjectVoucherRelationAllowUseTypeEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}

package org.example.musk.enums.integralMall;


import lombok.Getter;

import java.util.Arrays;

/**
 * 积分商城 申请兑换状态
 */
@Getter
public enum IntegralMallApplyStatusEnum {

    PENNING(1,"处理中"),
    SUCC(2,"处理成功"),
    FAIL(3,"处理失败"),
    ;

    private Integer status;
    private String desc;

    IntegralMallApplyStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static final IntegralMallApplyStatusEnum getEnumsByStatus(short status){
        return Arrays.stream(IntegralMallApplyStatusEnum.values()).filter(k->k.getStatus() == status).findFirst().orElse(null);
    }

}

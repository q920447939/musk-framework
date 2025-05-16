package org.example.musk.enums.invest;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年06月28日
 */
@Getter
public enum InvestModelEnums {
    //收益模式(1:单利、2:复利)
    SINGLE_INTEREST((short)1,"单利"),
    COMPOUND_INTEREST((short)2,"复利"),
    ;

    private Short model;
    private String desc;


    InvestModelEnums(Short model, String desc) {
        this.model = model;
        this.desc = desc;
    }

    public static final InvestModelEnums getInvestModelEnumsByModel(short model){
        return Arrays.stream(InvestModelEnums.values()).filter(k->k.getModel().equals(model)).findFirst().orElseThrow(()->{
            throw new RuntimeException("收益模式不正確");
        });
    }
}

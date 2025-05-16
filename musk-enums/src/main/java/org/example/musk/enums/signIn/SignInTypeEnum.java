package org.example.musk.enums.signIn;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SignInTypeEnum {

    BASE(1,"签到基础奖励"),
    EXTRA(2,"签到额外奖励"),
    ;

    private Integer type;
    private String desc;

    SignInTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static SignInTypeEnum getEnumsByType(short type){
        return Arrays.stream(SignInTypeEnum.values()).filter(k->k.getType() == type).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取数据");
        });
    }
}

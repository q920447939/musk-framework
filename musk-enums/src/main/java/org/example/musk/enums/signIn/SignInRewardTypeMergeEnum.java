package org.example.musk.enums.signIn;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SignInRewardTypeMergeEnum {
    INTEGRAL(SignInTypeEnum.BASE, 1, "baseIntegralSignInStrategy", "签到基础奖励-积分奖励","积分"),
    USDT(SignInTypeEnum.BASE, 2, "baseUSDTSignInStrategy", "签到基础奖励-USDT奖励","USDT"),

    EXTRA_INTEGRAL(SignInTypeEnum.EXTRA, 1, "extraIntegralSignInStrategy", "签到额外奖励-积分奖励","积分"),
    EXTRA_USDT(SignInTypeEnum.EXTRA, 2, "extraUSDTSignInStrategy", "签到基础奖励-USDT奖励","USDT"),

    ;

    private SignInTypeEnum signInTypeEnum;
    private Integer type;
    private String rewardStrategy;
    private String desc;
    private String simpleDesc;

    SignInRewardTypeMergeEnum(SignInTypeEnum signInTypeEnum, Integer type, String rewardStrategy, String desc, String simpleDesc) {
        this.signInTypeEnum = signInTypeEnum;
        this.type = type;
        this.rewardStrategy = rewardStrategy;
        this.desc = desc;
        this.simpleDesc = simpleDesc;
    }

    public static SignInRewardTypeMergeEnum findSignInRewardTypeEnum(SignInTypeEnum signInTypeEnum, Integer type){
        return Arrays.stream(SignInRewardTypeMergeEnum.values()).filter(k->k.getSignInTypeEnum().equals(signInTypeEnum) && k.getType().equals(type)).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取奖励类型");
        });
    }
}

package org.example.musk.enums.recharge;

import lombok.Getter;

import java.util.Arrays;



@Getter
public enum MemberRechargeCurrencyEnums {


    USDT((short) 1, "USDT"),
    ;

    /**
     * 状态值
     */
    private final Short currency;
    /**
     * 状态名
     */
    private final String desc;


    /**
     * 会员充值状态枚举
     *
     * @param currency 状态
     * @param desc   desc
     */
    MemberRechargeCurrencyEnums(Short currency, String desc) {
        this.currency = currency;
        this.desc = desc;
    }


    public static MemberRechargeCurrencyEnums getEnumsByCurrency(short currency){
        return Arrays.stream(MemberRechargeCurrencyEnums.values()).filter(k->k.getCurrency() == currency).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取到数据");
        });
    }

}

package org.example.musk.enums.luckLottery;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum LuckLotteryPrizeStatusEnums {


    VALID((short) 1, "生效"),

    IN_VALID((short) 2, "失效");

    /**
     * 状态值
     */
    private final Short status;
    /**
     * 状态名
     */
    private final String desc;

    /**
     * 会员投资计划状态枚举
     *
     * @param status 状态
     * @param desc   desc
     */
    LuckLotteryPrizeStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static LuckLotteryPrizeStatusEnums getEnumsByType(short status){
        return Arrays.stream(LuckLotteryPrizeStatusEnums.values()).filter(k->k.getStatus().equals(status) ).findFirst().orElseThrow(()-> new RuntimeException("未获取到状态"));
    }

}

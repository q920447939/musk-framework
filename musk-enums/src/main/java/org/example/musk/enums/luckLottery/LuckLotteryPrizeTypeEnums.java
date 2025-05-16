package org.example.musk.enums.luckLottery;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum LuckLotteryPrizeTypeEnums {


    INTEGRAL((short) 1, "积分"),

    //VOUCHER((short) 2, "代金券"),
    EXPERIENCE((short) 3, "经验值"),
    THANKS((short) 4, "谢谢参与"),
    MATERIAL((short) 5, "实物");

    /**
     * 状态值
     */
    private final Short type;
    /**
     * 状态名
     */
    private final String desc;

    /**
     * 会员投资计划状态枚举
     *
     * @param type 状态
     * @param desc   desc
     */
    LuckLotteryPrizeTypeEnums(Short type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static LuckLotteryPrizeTypeEnums getEnumsByType(short type){
        return Arrays.stream(LuckLotteryPrizeTypeEnums.values()).filter(k->k.getType().equals(type) ).findFirst().orElseThrow(()-> new RuntimeException("未获取到状态"));
    }

}

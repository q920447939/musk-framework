package org.example.musk.enums.cash;

import lombok.Getter;

import java.util.Arrays;


/**
 * 提现类型枚举
 *
 * @author
 * @date 2024/07/17
 */
@Getter
public enum CashTypeEnums {


    USDT((short) 1, "USDT"),
    BANK((short) 2, "银行卡");


    private final Short type;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param type 类型
     * @param desc desc
     */
    CashTypeEnums(Short type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param type 类型
     * @return {@link CashTypeEnums }
     */
    public static CashTypeEnums getEnumsByType(short type){
        return Arrays.stream(CashTypeEnums.values()).filter(k->k.getType() == type).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取到提现类型");
        });
    }


}

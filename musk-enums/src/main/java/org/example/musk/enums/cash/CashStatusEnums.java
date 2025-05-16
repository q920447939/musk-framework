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
public enum CashStatusEnums {
    APPLYING((short) 1, "申请提现中"),
    SUCC((short) 2, "提现成功"),
    FAIL((short) 3, "提现失败"),
    CANCEL((short) 4, "取消提现");

    private final Short status;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param status 类型
     * @param desc desc
     */
    CashStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     * @return {@link CashStatusEnums }
     */
    public static CashStatusEnums getEnumsByType(short status){
        return Arrays.stream(CashStatusEnums.values()).filter(k->k.getStatus() == status).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取到提现状态");
        });
    }


}

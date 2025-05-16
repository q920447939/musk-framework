package org.example.musk.enums.recharge;

import lombok.Getter;

import java.util.Arrays;


/**
 * 会员充值状态枚举
 *
 * @author
 * @date 2024/07/08
 */
@Getter
public enum MemberRechargeStatusEnums {


    PENDING((short) 1, "待处理"),
    SUCC((short) 2, "成功"),
    FAIL_MANUAL((short) 3, "人工处理失败"),
    FAIL_APPROVE_TIME_OUT((short) 4, "审核超时失败"),
    CANCEL((short) 5, "作废");

    /**
     * 状态值
     */
    private final Short status;
    /**
     * 状态名
     */
    private final String desc;


    /**
     * 会员充值状态枚举
     *
     * @param status 状态
     * @param desc   desc
     */
    MemberRechargeStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public static MemberRechargeStatusEnums getEnumsByType(short status){
        return Arrays.stream(MemberRechargeStatusEnums.values()).filter(k->k.getStatus() == status).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取到会员充值状态");
        });
    }

}

package org.example.musk.enums.memberInvestPlan;

import com.musk.constant.enums.cash.CashStatusEnums;
import lombok.Getter;

import java.util.Arrays;

/**
 * 会员投资计划状态枚举
 *
 * @author
 * @date 2024/07/04
 */
@Getter
public enum MemberInvestPlanStatusEnums {


    RUNNING((short) 1, "进行中"),

    END((short) 2, "已结束"),
    CANCEL((short) 3, "已取消"),;

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
    MemberInvestPlanStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     * @return {@link CashStatusEnums }
     */
    public static MemberInvestPlanStatusEnums getEnumsByType(short status){
        return Arrays.stream(MemberInvestPlanStatusEnums.values()).filter(k->k.getStatus().equals(status) ).findFirst().orElseThrow(()-> new RuntimeException("未获取到状态"));
    }

}

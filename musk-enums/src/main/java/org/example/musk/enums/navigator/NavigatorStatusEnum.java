package org.example.musk.enums.navigator;


import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;


/**
 * 項目状态枚举
 */
@Getter
@ToString
public enum NavigatorStatusEnum {

    ENABLE( 0, "正常"),
    INVALID( 1, "停用");
    ;

    private Integer code;
    private String desc;

    NavigatorStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     * @return {@link CashStatusEnums }
     */
    public static NavigatorStatusEnum getEnumsByType(Integer status){
        return Arrays.stream(NavigatorStatusEnum.values()).filter(k->k.getCode().equals(status) ).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取到状态");
        });
    }

}

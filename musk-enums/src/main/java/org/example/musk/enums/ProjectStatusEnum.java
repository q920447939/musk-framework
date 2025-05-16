package org.example.musk.enums;

import com.musk.constant.enums.cash.CashStatusEnums;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;


/**
 * 項目状态枚举
 */
@Getter
@ToString
public enum ProjectStatusEnum {

    STOP(1,"暂停"),
    RUNNING(2,"运行中"),
    END(3,"已结束"),
    ;

    private Integer code;
    private String desc;

    ProjectStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     * @return {@link CashStatusEnums }
     */
    public static ProjectStatusEnum getEnumsByType(Integer status){
        return Arrays.stream(ProjectStatusEnum.values()).filter(k->k.getCode().equals(status) ).findFirst().orElseThrow(()->{
            throw new RuntimeException("未获取到状态");
        });
    }

}

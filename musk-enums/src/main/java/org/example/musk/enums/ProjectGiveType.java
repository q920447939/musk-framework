package org.example.musk.enums;

import lombok.Getter;


/**
 * 投资赠送类型
 */
@Getter
public enum ProjectGiveType {

    VOUCHER(1,"代金券"),
    INTEGRAL(2,"积分"),
    ;

    private Integer code;
    private String desc;

    ProjectGiveType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

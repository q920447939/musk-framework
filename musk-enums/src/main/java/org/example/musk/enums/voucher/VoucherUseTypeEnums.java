package org.example.musk.enums.voucher;


import lombok.Getter;


/**
 * 代金券使用类型
 */
@Getter
public enum VoucherUseTypeEnums {
    INVEST(1,"满减"),
    GIVE(2,"折扣"),
    ;

    private Integer type;
    private String desc;

    VoucherUseTypeEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}

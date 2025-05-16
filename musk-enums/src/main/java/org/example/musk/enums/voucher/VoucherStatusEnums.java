package org.example.musk.enums.voucher;


import lombok.Getter;

@Getter
public enum VoucherStatusEnums {
    ENABLE(1,"可使用"),
    OVERDUE(2,"已过期"),
    STOP(3,"暂停"),
    INVALID(4,"未生效"),
    UN_CAPACITY(5,"剩余数量不足"),
    ;

    private Integer status;
    private String desc;

    VoucherStatusEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}

package org.example.musk.enums.voucher;


import lombok.Getter;


/**
 * 代金券使用场景
 */
@Getter
public enum VoucherSceneEnums {
    INVEST(1,"投资"),
    ;

    private Integer scene;
    private String desc;

    VoucherSceneEnums(Integer scene, String desc) {
        this.scene = scene;
        this.desc = desc;
    }
}

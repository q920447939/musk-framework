package org.example.musk.enums.transfer;

import lombok.Getter;

import java.util.Arrays;


/**
 * 转账状态枚举
 *
 * @author
 * @date 2024/07/08
 */
@Getter
public enum TransferStatusEnums {


    SUCC((short) 1, "成功"),
    FAIL((short) 2, "失败");

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
    TransferStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static final TransferStatusEnums getTransferStatusEnumsByStatus(short status){
        return Arrays.stream(TransferStatusEnums.values()).filter(k->k.getStatus() == status).findFirst().orElse(null);
    }


}

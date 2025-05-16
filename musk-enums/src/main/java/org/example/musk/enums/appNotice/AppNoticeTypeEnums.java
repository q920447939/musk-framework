package org.example.musk.enums.appNotice;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum AppNoticeTypeEnums {

    POP_UP((short) 1, "弹窗公告"),

    ROLL((short) 2, "滚动公告");

    /**
     * 状态值
     */
    private final Short status;
    /**
     * 状态名
     */
    private final String desc;

    AppNoticeTypeEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static AppNoticeTypeEnums getEnumsByType(short status){
        return Arrays.stream(AppNoticeTypeEnums.values()).filter(k->k.getStatus().equals(status) ).findFirst().orElseThrow(()-> new RuntimeException("未获取到状态"));
    }

}

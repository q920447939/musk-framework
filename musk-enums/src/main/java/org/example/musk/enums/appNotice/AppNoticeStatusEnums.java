package org.example.musk.enums.appNotice;

import lombok.Getter;

import java.util.Arrays;


@Getter
public enum AppNoticeStatusEnums {


    VALID((short) 1, "生效"),

    IN_VALID((short) 2, "失效");

    /**
     * 状态值
     */
    private final Short status;
    /**
     * 状态名
     */
    private final String desc;

    AppNoticeStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static AppNoticeStatusEnums getEnumsByType(short status){
        return Arrays.stream(AppNoticeStatusEnums.values()).filter(k->k.getStatus().equals(status) ).findFirst().orElseThrow(()-> new RuntimeException("未获取到状态"));
    }

}

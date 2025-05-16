package org.example.musk.enums.appConfig;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年07月19日
 */
@Getter
public enum AppParamsConfigSystemEnums {
    APP((short) 1, "APP"),;

    private final Short system;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param system 类型
     * @param desc   desc
     */
    AppParamsConfigSystemEnums(Short system, String desc) {
        this.system = system;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     */
    public static AppParamsConfigSystemEnums getEnumsByType(short status) {
        return Arrays.stream(AppParamsConfigSystemEnums.values()).filter(k -> k.getSystem() == status).findFirst().orElseThrow(() -> new RuntimeException("未获取到枚举信息"));
    }

}

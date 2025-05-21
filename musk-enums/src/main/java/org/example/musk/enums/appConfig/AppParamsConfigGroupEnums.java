package org.example.musk.enums.appConfig;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年07月19日
 */
@Getter
public enum AppParamsConfigGroupEnums {
    CASH("cash", "提现"),
    TRANSFER("transfer", "转账"),
    REAL_NAME_APPLY("real_name_apply", "实名认证"),
    LANGUAGE("language", "语言"),
    LUCK_LOTTERY("luck_lottery", "抽奖"),
    NOTICE_CONFIG("notice_config", "公告"),
    LOGIN("login", "登录"),
    REGISTER("register", "注册"),
    INVEST("invest", "投资"),
    ;

    private final String group;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param group 类型
     * @param desc   desc
     */
    AppParamsConfigGroupEnums(String group, String desc) {
        this.group = group;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param group 类型
     * @return {@link CashStatusEnums }
     */
    public static AppParamsConfigGroupEnums getEnumsByType(String group) {
        return Arrays.stream(AppParamsConfigGroupEnums.values()).filter(k -> k.getGroup().equals(group)).findFirst().orElseThrow(() -> {
            throw new RuntimeException("未获取到枚举信息");
        });
    }

}

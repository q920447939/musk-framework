package org.example.musk.enums.appConfig;

import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年07月19日
 */
@Getter
public enum AppParamConfigEnums {
    INVALID((short) 0, "失效"),
    VALID((short) 1, "生效"),
    ;

    private final Short status;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param status 类型
     * @param desc   desc
     */
    AppParamConfigEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     */
    public static AppParamConfigEnums getEnumsByType(short status) {
        return Arrays.stream(AppParamConfigEnums.values()).filter(k -> k.getStatus() == status).findFirst().orElseThrow(() -> {
            throw new RuntimeException("未获取到状态信息");
        });
    }

}

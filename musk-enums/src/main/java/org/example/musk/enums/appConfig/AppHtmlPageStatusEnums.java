package org.example.musk.enums.appConfig;

import com.musk.constant.enums.cash.CashStatusEnums;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年07月19日
 */
@Getter
public enum AppHtmlPageStatusEnums {
    ENABLE((short) 0, "正常"),
    INVALID((short) 1, "停用");

    private final Short status;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param status 类型
     * @param desc   desc
     */
    AppHtmlPageStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     * @return {@link CashStatusEnums }
     */
    public static AppHtmlPageStatusEnums getEnumsByType(short status) {
        return Arrays.stream(AppHtmlPageStatusEnums.values()).filter(k -> k.getStatus() == status).findFirst().orElseThrow(() -> {
            throw new RuntimeException("未获取到状态信息");
        });
    }

}

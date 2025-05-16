package org.example.musk.enums.appConfig;

import com.musk.constant.enums.cash.CashStatusEnums;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年07月19日
 */
@Getter
public enum AppTutorialStatusEnums {
    RELEASE((short) 1, "发布"),
    DRAFT((short) 2, "草稿"),
    CANCEL((short) 3, "取消提现");

    private final Short status;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param status 类型
     * @param desc   desc
     */
    AppTutorialStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     * @return {@link CashStatusEnums }
     */
    public static AppTutorialStatusEnums getEnumsByType(short status) {
        return Arrays.stream(AppTutorialStatusEnums.values()).filter(k -> k.getStatus() == status).findFirst().orElseThrow(() -> {
            throw new RuntimeException("未获取到状态信息");
        });
    }

}

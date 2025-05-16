package org.example.musk.enums.appConfig;

import com.musk.constant.enums.cash.CashStatusEnums;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AppVersionForceUpgradeStatusEnums {
    FORCE((short) 1, "强制更新"),
    UN_FORCE((short) 2, "非强制更新");

    private final Short status;

    private final String desc;


    AppVersionForceUpgradeStatusEnums(Short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 获得枚举通过类型
     *
     * @param status 类型
     * @return {@link CashStatusEnums }
     */
    public static AppVersionForceUpgradeStatusEnums getEnumsByType(short status) {
        return Arrays.stream(AppVersionForceUpgradeStatusEnums.values()).filter(k -> k.getStatus() == status).findFirst().orElseThrow(() -> {
            throw new RuntimeException("未获取到状态信息");
        });
    }

}

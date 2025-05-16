package org.example.musk.enums.appConfig;

import lombok.Getter;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Arrays;

/**
 * @Description:
 * @date 2024年07月19日
 */
@Getter
public enum AppParamsConfigTypeEnums {
    DAILY_CASH_APPLY_LIMIT(AppParamsConfigGroupEnums.CASH, "daily_cash_apply_limit", "每日申请提现次数"),
    CASH_HANDLING_FEE(AppParamsConfigGroupEnums.CASH, "cash_handling_fee", "提现手续费"),
    TRANSFER_HANDLING_FEE(AppParamsConfigGroupEnums.TRANSFER, "transfer_handling_fee", "转账手续费"),
    COUNTRY_LIST(AppParamsConfigGroupEnums.REAL_NAME_APPLY, "country_list", "国家列表"),
    COUNTRY_ID_CARD_TYPE(AppParamsConfigGroupEnums.REAL_NAME_APPLY, "country_id_card_type", "认证类型"),
    MOCK_NICK_NAME_COUNTRY(AppParamsConfigGroupEnums.LANGUAGE, "mock_nick_name_country", "模拟国家用户姓名"),

    //抽奖开始
    MOCK_USER_DAILY_MAX_NUMBER(AppParamsConfigGroupEnums.LUCK_LOTTERY, "mock_user_daily_max_number", "每日模拟抽奖用户的最大值"),
    MOCK_USER_DAILY_MAX(AppParamsConfigGroupEnums.LUCK_LOTTERY, "mock_user_daily_max", "单次模拟抽奖用户最大值"),

    //公告开始
    NOTICE_FETCH_MAX_NUM(AppParamsConfigGroupEnums.NOTICE_CONFIG, "notice_fetch_max_num", "单次取公告的最大数"),



    //登录开始
    LOGIN_LIMIT_RATE(AppParamsConfigGroupEnums.LOGIN, "login_limit_rate", "登录频率限制"),
    LOGIN_VALIDATE_CODE_SKIP(AppParamsConfigGroupEnums.LOGIN, "login_validate_code_skip", "登录验证码跳过"),

    //注册开始
    REGISTER_VALIDATE_CODE_SKIP(AppParamsConfigGroupEnums.REGISTER, "register_validate_code_skip", "注册验证码跳过"),

    //投资相关
    INVEST_PLAN_CANCEL(AppParamsConfigGroupEnums.INVEST, "invest_plan_cancel", "投资计划取消"),
    INVEST_PLAN_CANCEL_DAILY_LIMIT(AppParamsConfigGroupEnums.INVEST, "invest_plan_cancel_daily_limit", "投资计划取消，每日可取消的上限次数"),
    ;

    private final AppParamsConfigGroupEnums appParamsConfigGroupEnum;
    private final String type;

    private final String desc;


    /**
     * 提现类型枚举
     *
     * @param type 类型
     * @param desc desc
     */
    AppParamsConfigTypeEnums(AppParamsConfigGroupEnums appParamsConfigGroupEnum, String type, String desc) {
        this.appParamsConfigGroupEnum = appParamsConfigGroupEnum;
        this.type = type;
        this.desc = desc;
    }

    public static AppParamsConfigTypeEnums fromType(String type) {
        return Arrays.stream(AppParamsConfigTypeEnums.values()).filter(k-> StrUtil.equals(type, k.getType())).findFirst().orElseThrow(()-> new RuntimeException("未获取到枚举信息"));
    }


}

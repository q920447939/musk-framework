package org.example.musk.framework.permission.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 系统参数分组枚举
 * 
 * 定义系统参数的分组，用于权限控制
 *
 * @author musk-framework-permission
 */
@Getter
public enum SystemParamGroupEnum {
    
    LOGGING("LOGGING", "日志配置", Arrays.asList("app_log_url", "log_level", "log_retention_days")),
    PAYMENT("PAYMENT", "支付配置", Arrays.asList("payment_gateway_url", "payment_timeout", "refund_policy")),
    NOTIFICATION("NOTIFICATION", "通知配置", Arrays.asList("notification_settings", "email_template", "sms_template")),
    SYSTEM("SYSTEM", "系统配置", Arrays.asList("maintenance_time", "system_mode", "version_display")),
    // 可以根据需要扩展更多分组
    ;
    
    /**
     * 分组编码
     */
    private final String code;
    
    /**
     * 分组描述
     */
    private final String desc;
    
    /**
     * 分组包含的参数列表
     */
    private final List<String> params;
    
    SystemParamGroupEnum(String code, String desc, List<String> params) {
        this.code = code;
        this.desc = desc;
        this.params = params;
    }
    
    /**
     * 通过编码获取枚举
     *
     * @param code 编码
     * @return 枚举
     */
    public static SystemParamGroupEnum getByCode(String code) {
        return Arrays.stream(SystemParamGroupEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 通过参数key查找所属的分组
     *
     * @param paramKey 参数key
     * @return 分组枚举，如果未找到则返回null
     */
    public static SystemParamGroupEnum findByParamKey(String paramKey) {
        return Arrays.stream(SystemParamGroupEnum.values())
                .filter(group -> group.getParams().contains(paramKey))
                .findFirst()
                .orElse(null);
    }
}

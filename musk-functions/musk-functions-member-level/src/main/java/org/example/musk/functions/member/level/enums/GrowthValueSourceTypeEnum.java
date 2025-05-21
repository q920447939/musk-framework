package org.example.musk.functions.member.level.enums;

import lombok.Getter;

/**
 * 成长值来源类型枚举
 *
 * @author musk-functions-member-level
 */
@Getter
public enum GrowthValueSourceTypeEnum {
    
    /**
     * 消费
     */
    CONSUMPTION(1, "消费"),
    
    /**
     * 活动
     */
    ACTIVITY(2, "活动"),
    
    /**
     * 签到
     */
    SIGN_IN(3, "签到"),
    
    /**
     * 任务
     */
    TASK(4, "任务"),
    
    /**
     * 管理员调整
     */
    ADMIN_ADJUST(5, "管理员调整"),
    
    /**
     * 其他
     */
    OTHER(6, "其他");
    
    private final Integer value;
    private final String name;
    
    GrowthValueSourceTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    
    /**
     * 通过值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static GrowthValueSourceTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (GrowthValueSourceTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}

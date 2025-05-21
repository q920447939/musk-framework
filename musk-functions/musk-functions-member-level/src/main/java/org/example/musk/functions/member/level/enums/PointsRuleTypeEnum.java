package org.example.musk.functions.member.level.enums;

import lombok.Getter;

/**
 * 积分规则类型枚举
 *
 * @author musk-functions-member-level
 */
@Getter
public enum PointsRuleTypeEnum {
    
    /**
     * 消费积分
     */
    CONSUMPTION(1, "消费积分"),
    
    /**
     * 活动积分
     */
    ACTIVITY(2, "活动积分"),
    
    /**
     * 签到积分
     */
    SIGN_IN(3, "签到积分"),
    
    /**
     * 任务积分
     */
    TASK(4, "任务积分"),
    
    /**
     * 其他
     */
    OTHER(5, "其他");
    
    private final Integer value;
    private final String name;
    
    PointsRuleTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    
    /**
     * 通过值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static PointsRuleTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PointsRuleTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}

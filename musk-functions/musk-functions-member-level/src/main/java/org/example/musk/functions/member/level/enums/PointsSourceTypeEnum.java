package org.example.musk.functions.member.level.enums;

import lombok.Getter;

/**
 * 积分来源类型枚举
 *
 * @author musk-functions-member-level
 */
@Getter
public enum PointsSourceTypeEnum {
    
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
     * 兑换
     */
    EXCHANGE(5, "兑换"),
    
    /**
     * 退款
     */
    REFUND(6, "退款"),
    
    /**
     * 管理员调整
     */
    ADMIN_ADJUST(7, "管理员调整"),
    
    /**
     * 其他
     */
    OTHER(8, "其他");
    
    private final Integer value;
    private final String name;
    
    PointsSourceTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    
    /**
     * 通过值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static PointsSourceTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PointsSourceTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}

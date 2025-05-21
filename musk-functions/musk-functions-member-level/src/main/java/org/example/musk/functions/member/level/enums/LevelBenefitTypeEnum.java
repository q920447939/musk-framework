package org.example.musk.functions.member.level.enums;

import lombok.Getter;

/**
 * 会员等级权益类型枚举
 *
 * @author musk-functions-member-level
 */
@Getter
public enum LevelBenefitTypeEnum {
    
    /**
     * 折扣率
     */
    DISCOUNT_RATE(1, "折扣率"),
    
    /**
     * 免邮次数
     */
    FREE_SHIPPING(2, "免邮次数"),
    
    /**
     * 生日礼
     */
    BIRTHDAY_GIFT(3, "生日礼"),
    
    /**
     * 专属客服
     */
    VIP_SERVICE(4, "专属客服"),
    
    /**
     * 积分加速
     */
    POINTS_ACCELERATION(5, "积分加速"),
    
    /**
     * 自定义权益
     */
    CUSTOM_BENEFIT(6, "自定义权益");
    
    private final Integer value;
    private final String name;
    
    LevelBenefitTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    
    /**
     * 通过值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static LevelBenefitTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (LevelBenefitTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}

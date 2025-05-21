package org.example.musk.functions.member.level.enums;

import lombok.Getter;

/**
 * 积分变更类型枚举
 *
 * @author musk-functions-member-level
 */
@Getter
public enum PointsChangeTypeEnum {
    
    /**
     * 增加
     */
    ADD(1, "增加"),
    
    /**
     * 减少
     */
    DEDUCT(2, "减少"),
    
    /**
     * 冻结
     */
    FREEZE(3, "冻结"),
    
    /**
     * 解冻
     */
    UNFREEZE(4, "解冻"),
    
    /**
     * 过期
     */
    EXPIRE(5, "过期");
    
    private final Integer value;
    private final String name;
    
    PointsChangeTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    
    /**
     * 通过值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static PointsChangeTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PointsChangeTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }
}

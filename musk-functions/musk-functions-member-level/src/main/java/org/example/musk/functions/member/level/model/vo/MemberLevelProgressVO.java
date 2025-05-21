package org.example.musk.functions.member.level.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员等级进度 VO
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelProgressVO {
    
    /**
     * 会员ID
     */
    private Integer memberId;
    
    /**
     * 当前等级ID
     */
    private Integer currentLevelId;
    
    /**
     * 当前等级名称
     */
    private String currentLevelName;
    
    /**
     * 当前等级值
     */
    private Integer currentLevelValue;
    
    /**
     * 当前成长值
     */
    private Integer currentGrowthValue;
    
    /**
     * 当前等级成长值门槛
     */
    private Integer currentLevelThreshold;
    
    /**
     * 下一等级ID
     */
    private Integer nextLevelId;
    
    /**
     * 下一等级名称
     */
    private String nextLevelName;
    
    /**
     * 下一等级值
     */
    private Integer nextLevelValue;
    
    /**
     * 下一等级成长值门槛
     */
    private Integer nextLevelThreshold;
    
    /**
     * 距离下一等级还需成长值
     */
    private Integer growthValueToNextLevel;
    
    /**
     * 升级进度百分比
     */
    private Integer upgradeProgressPercent;
    
    /**
     * 是否已是最高等级
     */
    private Boolean isHighestLevel;
}

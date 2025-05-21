package org.example.musk.functions.member.level.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会员成长值 VO
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberGrowthValueVO {
    
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
     * 总成长值
     */
    private Integer totalGrowthValue;
    
    /**
     * 当前周期成长值
     */
    private Integer currentPeriodGrowthValue;
    
    /**
     * 下一等级门槛
     */
    private Integer nextLevelThreshold;
    
    /**
     * 下一等级ID
     */
    private Integer nextLevelId;
    
    /**
     * 下一等级名称
     */
    private String nextLevelName;
    
    /**
     * 距离下一等级还需成长值
     */
    private Integer growthValueToNextLevel;
    
    /**
     * 升级进度百分比
     */
    private Integer upgradeProgressPercent;
    
    /**
     * 周期开始时间
     */
    private LocalDateTime periodStartTime;
    
    /**
     * 周期结束时间
     */
    private LocalDateTime periodEndTime;
}

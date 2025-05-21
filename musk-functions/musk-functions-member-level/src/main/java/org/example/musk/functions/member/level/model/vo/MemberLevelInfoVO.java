package org.example.musk.functions.member.level.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 会员等级信息 VO
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelInfoVO {
    
    /**
     * 会员ID
     */
    private Integer memberId;
    
    /**
     * 当前等级ID
     */
    private Integer currentLevelId;
    
    /**
     * 当前等级编码
     */
    private String currentLevelCode;
    
    /**
     * 当前等级名称
     */
    private String currentLevelName;
    
    /**
     * 当前等级图标ID
     */
    private Integer currentLevelIconId;
    
    /**
     * 当前等级值
     */
    private Integer currentLevelValue;
    
    /**
     * 当前等级颜色
     */
    private String currentLevelColor;
    
    /**
     * 当前等级描述
     */
    private String currentLevelDescription;
    
    /**
     * 当前成长值
     */
    private Integer currentGrowthValue;
    
    /**
     * 下一等级ID
     */
    private Integer nextLevelId;
    
    /**
     * 下一等级名称
     */
    private String nextLevelName;
    
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
     * 当前等级权益列表
     */
    private List<MemberLevelBenefitVO> benefits;
}

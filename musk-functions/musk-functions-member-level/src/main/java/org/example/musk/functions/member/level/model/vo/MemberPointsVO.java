package org.example.musk.functions.member.level.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员积分 VO
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointsVO {
    
    /**
     * 会员ID
     */
    private Integer memberId;
    
    /**
     * 可用积分
     */
    private Integer availablePoints;
    
    /**
     * 冻结积分
     */
    private Integer frozenPoints;
    
    /**
     * 总积分(累计获得)
     */
    private Integer totalPoints;
    
    /**
     * 已使用积分
     */
    private Integer usedPoints;
    
    /**
     * 已过期积分
     */
    private Integer expiredPoints;
}

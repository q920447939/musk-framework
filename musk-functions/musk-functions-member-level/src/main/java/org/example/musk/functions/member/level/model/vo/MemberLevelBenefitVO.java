package org.example.musk.functions.member.level.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员等级权益 VO
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelBenefitVO {
    
    /**
     * 权益ID
     */
    private Integer id;
    
    /**
     * 等级ID
     */
    private Integer levelId;
    
    /**
     * 权益类型(1:折扣率 2:免邮次数 3:生日礼 4:专属客服 5:积分加速 6:自定义权益)
     */
    private Integer benefitType;
    
    /**
     * 权益类型名称
     */
    private String benefitTypeName;
    
    /**
     * 权益名称
     */
    private String benefitName;
    
    /**
     * 权益值
     */
    private String benefitValue;
    
    /**
     * 权益图标ID
     */
    private Integer benefitIconId;
    
    /**
     * 权益描述
     */
    private String benefitDescription;
    
    /**
     * 显示顺序
     */
    private Integer displayIndex;
}

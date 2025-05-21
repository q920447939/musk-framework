package org.example.musk.functions.member.level.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 会员等级权益创建请求 VO
 *
 * @author musk-functions-member-level
 */
@Data
public class MemberLevelBenefitCreateReqVO {
    
    /**
     * 等级ID
     */
    @NotNull(message = "等级ID不能为空")
    private Integer levelId;
    
    /**
     * 权益类型(1:折扣率 2:免邮次数 3:生日礼 4:专属客服 5:积分加速 6:自定义权益)
     */
    @NotNull(message = "权益类型不能为空")
    private Integer benefitType;
    
    /**
     * 权益名称
     */
    @NotBlank(message = "权益名称不能为空")
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
    
    /**
     * 状态(0:启用 1:禁用)
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}

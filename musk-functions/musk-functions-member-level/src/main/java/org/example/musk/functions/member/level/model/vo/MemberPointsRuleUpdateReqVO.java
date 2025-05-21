package org.example.musk.functions.member.level.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分规则更新请求 VO
 *
 * @author musk-functions-member-level
 */
@Data
public class MemberPointsRuleUpdateReqVO {
    
    /**
     * 规则ID
     */
    @NotNull(message = "规则ID不能为空")
    private Integer id;
    
    /**
     * 规则编码
     */
    @NotBlank(message = "规则编码不能为空")
    private String ruleCode;
    
    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;
    
    /**
     * 规则类型(1:消费积分 2:活动积分 3:签到积分 4:任务积分 5:其他)
     */
    @NotNull(message = "规则类型不能为空")
    private Integer ruleType;
    
    /**
     * 积分值
     */
    @NotNull(message = "积分值不能为空")
    private Integer pointsValue;
    
    /**
     * 成长值
     */
    private Integer growthValue;
    
    /**
     * 规则公式
     */
    private String ruleFormula;
    
    /**
     * 规则描述
     */
    private String ruleDescription;
    
    /**
     * 生效开始时间
     */
    private LocalDateTime effectiveStartTime;
    
    /**
     * 生效结束时间
     */
    private LocalDateTime effectiveEndTime;
    
    /**
     * 每日限制次数
     */
    private Integer dailyLimit;
    
    /**
     * 状态(0:启用 1:禁用)
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}

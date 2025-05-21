package org.example.musk.functions.member.level.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 会员等级定义创建请求 VO
 *
 * @author musk-functions-member-level
 */
@Data
public class MemberLevelDefinitionCreateReqVO {
    
    /**
     * 等级编码
     */
    @NotBlank(message = "等级编码不能为空")
    private String levelCode;
    
    /**
     * 等级名称
     */
    @NotBlank(message = "等级名称不能为空")
    private String levelName;
    
    /**
     * 等级图标ID
     */
    private Integer levelIconId;
    
    /**
     * 等级值
     */
    @NotNull(message = "等级值不能为空")
    private Integer levelValue;
    
    /**
     * 成长值门槛
     */
    @NotNull(message = "成长值门槛不能为空")
    private Integer growthValueThreshold;
    
    /**
     * 等级描述
     */
    private String levelDescription;
    
    /**
     * 等级颜色(十六进制)
     */
    private String levelColor;
    
    /**
     * 显示顺序
     */
    private Integer displayIndex;
    
    /**
     * 状态(0:启用 1:禁用)
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
}

package org.example.musk.functions.member.level.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;

/**
 * 会员等级定义 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_level_definition")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelDefinitionDO extends DomainBaseDO {

    /**
     * 等级ID
     */
    @TableId
    private Integer id;

    /**
     * 等级编码
     */
    private String levelCode;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 等级图标ID
     */
    private Integer levelIconId;

    /**
     * 等级值
     */
    private Integer levelValue;

    /**
     * 成长值门槛
     */
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
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}

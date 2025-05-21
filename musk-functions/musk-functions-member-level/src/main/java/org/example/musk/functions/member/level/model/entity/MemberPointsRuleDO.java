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

import java.time.LocalDateTime;

/**
 * 积分规则 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_points_rule")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointsRuleDO extends DomainBaseDO {

    /**
     * 规则ID
     */
    @TableId
    private Integer id;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型(1:消费积分 2:活动积分 3:签到积分 4:任务积分 5:其他)
     */
    private Integer ruleType;

    /**
     * 积分值
     */
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
    private Integer status;
}

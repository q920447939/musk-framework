package org.example.musk.functions.invitation.dao.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员邀请奖励规则实体类
 *
 * @author musk-functions-member-invitation
 */
@TableName(value = "member_invitation_reward_rule", autoResultMap = true)
@KeySequence("member_invitation_reward_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInvitationRewardRuleDO extends DomainBaseDO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则描述
     */
    private String ruleDescription;

    /**
     * 触发事件(REGISTER:注册 FIRST_ORDER:首单 CONSUMPTION:消费)
     */
    private String triggerEvent;

    /**
     * 触发条件(JSON格式)
     */
    private String triggerCondition;

    /**
     * 奖励对象(1:邀请人 2:被邀请人 3:双方)
     */
    private Integer rewardTarget;

    /**
     * 奖励类型(POINTS:积分 GROWTH:成长值 COUPON:优惠券 MEMBER_DAYS:会员天数)
     */
    private String rewardType;

    /**
     * 奖励值
     */
    private BigDecimal rewardValue;

    /**
     * 奖励配置(JSON格式)
     */
    private String rewardConfig;

    /**
     * 发放方式(1:立即发放 2:延迟发放)
     */
    private Integer delayType;

    /**
     * 延迟条件(JSON格式)
     */
    private String delayCondition;

    /**
     * 生效开始时间
     */
    private LocalDateTime effectiveStartTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime effectiveEndTime;

    /**
     * 状态(1:启用 2:禁用)
     */
    private Integer status;

    /**
     * 优先级
     */
    private Integer priority;

}

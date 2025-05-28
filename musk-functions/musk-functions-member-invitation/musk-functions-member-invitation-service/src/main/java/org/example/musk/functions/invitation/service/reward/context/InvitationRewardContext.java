package org.example.musk.functions.invitation.service.reward.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 邀请奖励上下文
 *
 * @author musk-functions-member-invitation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationRewardContext {

    /**
     * 奖励记录ID
     */
    private Long rewardRecordId;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 邀请关系ID
     */
    private Long invitationRelationId;

    /**
     * 获得奖励的会员ID
     */
    private Integer rewardMemberId;

    /**
     * 邀请人会员ID
     */
    private Integer inviterMemberId;

    /**
     * 被邀请人会员ID
     */
    private Integer inviteeMemberId;

    /**
     * 奖励类型
     */
    private String rewardType;

    /**
     * 奖励值
     */
    private BigDecimal rewardValue;

    /**
     * 奖励配置（JSON解析后的Map）
     */
    private Map<String, Object> rewardConfig;

    /**
     * 触发事件
     */
    private String triggerEvent;

    /**
     * 触发源ID
     */
    private String triggerSourceId;

    /**
     * 事件数据
     */
    private Map<String, Object> eventData;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 域ID
     */
    private Integer domainId;

}

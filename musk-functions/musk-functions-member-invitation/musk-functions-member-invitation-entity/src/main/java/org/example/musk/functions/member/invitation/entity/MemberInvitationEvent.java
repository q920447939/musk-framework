package org.example.musk.functions.member.invitation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 会员邀请事件
 *
 * @author musk-functions-member-invitation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInvitationEvent {

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * 邀请人会员ID
     */
    private Integer inviterMemberId;

    /**
     * 被邀请人会员ID
     */
    private Integer inviteeMemberId;

    /**
     * 邀请关系ID
     */
    private Long invitationRelationId;

    /**
     * 触发源ID
     */
    private String sourceId;

    /**
     * 事件数据
     */
    private Map<String, Object> eventData;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 域ID
     */
    private Integer domainId;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 注册渠道
     */
    private String registerChannel;

}

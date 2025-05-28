package org.example.musk.functions.invitation.event.publisher;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.invitation.dao.enums.InvitationTriggerEventEnum;
import org.example.musk.functions.member.invitation.entity.MemberInvitationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 会员邀请事件发布器
 *
 * @author musk-functions-member-invitation
 */
@Component
@Slf4j
public class MemberInvitationEventPublisher {

    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 发布注册邀请事件
     *
     * @param invitationCode 邀请码
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @param invitationRelationId 邀请关系ID
     * @param clientIp 客户端IP
     * @param registerChannel 注册渠道
     */
    public void publishRegisterEvent(String invitationCode, Integer inviterMemberId, Integer inviteeMemberId,
                                   Long invitationRelationId, String clientIp, String registerChannel) {
        publishEvent(InvitationTriggerEventEnum.REGISTER, invitationCode, inviterMemberId, inviteeMemberId,
                    invitationRelationId, String.valueOf(inviteeMemberId), null, clientIp, registerChannel);
    }

    /**
     * 发布首单邀请事件
     *
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @param invitationRelationId 邀请关系ID
     * @param orderId 订单ID
     * @param eventData 事件数据
     */
    public void publishFirstOrderEvent(Integer inviterMemberId, Integer inviteeMemberId,
                                     Long invitationRelationId, String orderId, Map<String, Object> eventData) {
        publishEvent(InvitationTriggerEventEnum.FIRST_ORDER, null, inviterMemberId, inviteeMemberId,
                    invitationRelationId, orderId, eventData, null, null);
    }

    /**
     * 发布消费邀请事件
     *
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @param invitationRelationId 邀请关系ID
     * @param orderId 订单ID
     * @param eventData 事件数据
     */
    public void publishConsumptionEvent(Integer inviterMemberId, Integer inviteeMemberId,
                                      Long invitationRelationId, String orderId, Map<String, Object> eventData) {
        publishEvent(InvitationTriggerEventEnum.CONSUMPTION, null, inviterMemberId, inviteeMemberId,
                    invitationRelationId, orderId, eventData, null, null);
    }

    /**
     * 发布邀请事件
     *
     * @param eventType 事件类型
     * @param invitationCode 邀请码
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @param invitationRelationId 邀请关系ID
     * @param sourceId 触发源ID
     * @param eventData 事件数据
     * @param clientIp 客户端IP
     * @param registerChannel 注册渠道
     */
    private void publishEvent(InvitationTriggerEventEnum eventType, String invitationCode,
                            Integer inviterMemberId, Integer inviteeMemberId, Long invitationRelationId,
                            String sourceId, Map<String, Object> eventData, String clientIp, String registerChannel) {
        try {
            // 创建事件
            MemberInvitationEvent event = MemberInvitationEvent.builder()
                    .eventType(eventType.getCode())
                    .invitationCode(invitationCode)
                    .inviterMemberId(inviterMemberId)
                    .inviteeMemberId(inviteeMemberId)
                    .invitationRelationId(invitationRelationId)
                    .sourceId(sourceId)
                    .eventData(eventData)
                    .eventTime(LocalDateTime.now())
                    .tenantId(ThreadLocalTenantContext.getTenantId())
                    .domainId(ThreadLocalTenantContext.getDomainId())
                    .clientIp(clientIp)
                    .registerChannel(registerChannel)
                    .build();

            // 发布事件
            eventPublisher.publishEvent(event);

            log.info("发布邀请事件成功，eventType={}, inviterMemberId={}, inviteeMemberId={}, sourceId={}",
                    eventType.getCode(), inviterMemberId, inviteeMemberId, sourceId);

        } catch (Exception e) {
            log.error("发布邀请事件失败，eventType={}, inviterMemberId={}, inviteeMemberId={}, sourceId={}",
                    eventType.getCode(), inviterMemberId, inviteeMemberId, sourceId, e);
        }
    }

}

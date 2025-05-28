/*
package org.example.musk.functions.invitation.event.listener;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.tx.DsPropagation;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.invitation.dao.enums.InvitationTriggerEventEnum;
import org.example.musk.functions.invitation.service.InvitationRewardService;
import org.example.musk.functions.member.invitation.entity.MemberInvitationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

*/
/**
 * 会员邀请事件监听器
 *
 * @author musk-functions-member-invitation
 *//*

@Component
@Slf4j
public class MemberInvitationEventListener {

    @Resource
    private InvitationRewardService invitationRewardService;

    */
/**
     * 处理会员邀请事件
     *
     * @param event 邀请事件
     *//*

    @EventListener
    @Async
    @DSTransactional(propagation = DsPropagation.REQUIRED, rollbackFor = Exception.class)
    public void handleMemberInvitationEvent(MemberInvitationEvent event) {
        try {
            log.info("处理会员邀请事件，eventType={}, inviterMemberId={}, inviteeMemberId={}, sourceId={}",
                    event.getEventType(), event.getInviterMemberId(), event.getInviteeMemberId(), event.getSourceId());

            // 验证事件类型
            InvitationTriggerEventEnum triggerEvent = InvitationTriggerEventEnum.fromCode(event.getEventType());
            if (triggerEvent == null) {
                log.warn("不支持的邀请事件类型，eventType={}", event.getEventType());
                return;
            }

            // 处理奖励
            invitationRewardService.processInvitationReward(event);

            log.info("会员邀请事件处理完成，eventType={}, inviterMemberId={}, inviteeMemberId={}",
                    event.getEventType(), event.getInviterMemberId(), event.getInviteeMemberId());

        } catch (Exception e) {
            log.error("处理会员邀请事件异常，eventType={}, inviterMemberId={}, inviteeMemberId={}",
                    event.getEventType(), event.getInviterMemberId(), event.getInviteeMemberId(), e);
            // 这里可以考虑发送告警或者重试机制
        }
    }

}
*/

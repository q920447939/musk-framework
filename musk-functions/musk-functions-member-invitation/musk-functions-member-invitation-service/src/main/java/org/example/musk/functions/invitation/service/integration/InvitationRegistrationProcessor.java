package org.example.musk.functions.invitation.service.integration;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationCodeDO;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRelationDO;
import org.example.musk.functions.invitation.event.publisher.MemberInvitationEventPublisher;
import org.example.musk.functions.invitation.service.InvitationCodeService;
import org.example.musk.functions.invitation.service.InvitationRelationService;
import org.example.musk.functions.invitation.service.InvitationSecurityService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 邀请注册处理器
 *
 * 集成到现有注册流程中，处理邀请码相关逻辑
 *
 * @author musk-functions-member-invitation
 */
@Component
@Slf4j
public class InvitationRegistrationProcessor {

    @Resource
    private InvitationCodeService invitationCodeService;

    @Resource
    private InvitationRelationService invitationRelationService;

    @Resource
    private InvitationSecurityService invitationSecurityService;

    @Resource
    private MemberInvitationEventPublisher eventPublisher;

    /**
     * 处理注册时的邀请逻辑
     *
     * @param memberId 新注册的会员ID
     * @param invitationCode 邀请码
     * @param clientIp 客户端IP
     * @param registerChannel 注册渠道
     * @return 是否处理成功
     */
    public boolean processRegistrationInvitation(Integer memberId, String invitationCode,
                                               String clientIp, String registerChannel) {
        // 如果没有邀请码，直接返回成功
        if (StrUtil.isBlank(invitationCode)) {
            log.debug("注册时未提供邀请码，跳过邀请处理，memberId={}", memberId);
            return true;
        }

        try {
            log.info("开始处理注册邀请，memberId={}, invitationCode={}, clientIp={}",
                    memberId, invitationCode, clientIp);

            // 1. 安全检查
            if (!invitationSecurityService.checkRegistrationSecurity(memberId, clientIp, registerChannel)) {
                log.warn("注册安全检查失败，memberId={}, clientIp={}", memberId, clientIp);
                return false;
            }

            // 2. 验证邀请码
            MemberInvitationCodeDO codeEntity = invitationCodeService.validateInvitationCode(invitationCode);
            if (codeEntity == null) {
                log.warn("邀请码无效，memberId={}, invitationCode={}", memberId, invitationCode);
                return false;
            }

            // 3. 验证邀请关系合法性
            if (!invitationRelationService.validateInvitationRelation(codeEntity.getInviterMemberId(), memberId)) {
                log.warn("邀请关系不合法，inviterMemberId={}, inviteeMemberId={}",
                        codeEntity.getInviterMemberId(), memberId);
                return false;
            }

            // 4. 使用邀请码
            if (!invitationCodeService.useInvitationCode(invitationCode)) {
                log.warn("使用邀请码失败，invitationCode={}", invitationCode);
                return false;
            }

            // 5. 创建邀请关系
            MemberInvitationRelationDO relationEntity = invitationRelationService.createInvitationRelation(
                    invitationCode, codeEntity.getInviterMemberId(), memberId, clientIp, registerChannel);

            if (relationEntity == null) {
                log.error("创建邀请关系失败，invitationCode={}, inviterMemberId={}, inviteeMemberId={}",
                        invitationCode, codeEntity.getInviterMemberId(), memberId);
                return false;
            }

            // 6. 发布注册邀请事件
            eventPublisher.publishRegisterEvent(invitationCode, codeEntity.getInviterMemberId(),
                    memberId, relationEntity.getId(), clientIp, registerChannel);

            log.info("注册邀请处理成功，memberId={}, inviterMemberId={}, relationId={}",
                    memberId, codeEntity.getInviterMemberId(), relationEntity.getId());

            return true;

        } catch (Exception e) {
            log.error("处理注册邀请异常，memberId={}, invitationCode={}", memberId, invitationCode, e);
            return false;
        }
    }

    /**
     * 检查会员是否通过邀请注册
     *
     * @param memberId 会员ID
     * @return 是否通过邀请注册
     */
    public boolean isInvitedMember(Integer memberId) {
        if (memberId == null) {
            return false;
        }

        MemberInvitationRelationDO relation = invitationRelationService.getInvitationRelationByInvitee(memberId);
        return relation != null;
    }

    /**
     * 获取会员的邀请人
     *
     * @param memberId 会员ID
     * @return 邀请人会员ID（null表示非邀请注册）
     */
    public Integer getInviterMemberId(Integer memberId) {
        if (memberId == null) {
            return null;
        }

        MemberInvitationRelationDO relation = invitationRelationService.getInvitationRelationByInvitee(memberId);
        return relation != null ? relation.getInviterMemberId() : null;
    }

}

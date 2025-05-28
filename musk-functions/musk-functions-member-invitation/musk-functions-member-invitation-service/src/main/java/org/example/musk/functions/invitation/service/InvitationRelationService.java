package org.example.musk.functions.invitation.service;

import org.example.musk.functions.invitation.dao.entity.MemberInvitationRelationDO;

import java.util.List;

/**
 * 邀请关系服务接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationRelationService {

    /**
     * 创建邀请关系
     *
     * @param invitationCode 邀请码
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @param registerIp 注册IP
     * @param registerChannel 注册渠道
     * @return 邀请关系
     */
    MemberInvitationRelationDO createInvitationRelation(String invitationCode, Integer inviterMemberId, 
                                                        Integer inviteeMemberId, String registerIp, 
                                                        String registerChannel);

    /**
     * 根据被邀请人查询邀请关系
     *
     * @param inviteeMemberId 被邀请人会员ID
     * @return 邀请关系
     */
    MemberInvitationRelationDO getInvitationRelationByInvitee(Integer inviteeMemberId);

    /**
     * 根据邀请人查询邀请关系列表
     *
     * @param inviterMemberId 邀请人会员ID
     * @return 邀请关系列表
     */
    List<MemberInvitationRelationDO> getInvitationRelationsByInviter(Integer inviterMemberId);

    /**
     * 根据邀请码查询邀请关系列表
     *
     * @param invitationCode 邀请码
     * @return 邀请关系列表
     */
    List<MemberInvitationRelationDO> getInvitationRelationsByCode(String invitationCode);

    /**
     * 统计邀请人的邀请数量
     *
     * @param inviterMemberId 邀请人会员ID
     * @return 邀请数量
     */
    Long countInvitationsByInviter(Integer inviterMemberId);

    /**
     * 检查是否存在邀请关系
     *
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @return 是否存在
     */
    boolean existsInvitationRelation(Integer inviterMemberId, Integer inviteeMemberId);

    /**
     * 验证邀请关系的合法性
     *
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @return 验证结果
     */
    boolean validateInvitationRelation(Integer inviterMemberId, Integer inviteeMemberId);

    /**
     * 失效邀请关系
     *
     * @param id 邀请关系ID
     * @return 是否失效成功
     */
    boolean invalidateInvitationRelation(Long id);

    /**
     * 根据ID获取邀请关系
     *
     * @param id 邀请关系ID
     * @return 邀请关系
     */
    MemberInvitationRelationDO getInvitationRelationById(Long id);

}

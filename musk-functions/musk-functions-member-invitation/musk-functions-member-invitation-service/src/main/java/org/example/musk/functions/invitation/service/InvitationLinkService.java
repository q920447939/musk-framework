package org.example.musk.functions.invitation.service;

import org.example.musk.functions.invitation.service.dto.InvitationLinkDTO;

/**
 * 邀请链接服务接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationLinkService {

    /**
     * 生成邀请链接
     *
     * @param memberId 会员ID
     * @param platform 平台类型（WEB、APP、H5等）
     * @return 邀请链接信息
     */
    InvitationLinkDTO generateInvitationLink(Integer memberId, String platform);

    /**
     * 生成邀请链接（使用指定邀请码）
     *
     * @param invitationCode 邀请码
     * @param platform 平台类型
     * @return 邀请链接信息
     */
    InvitationLinkDTO generateInvitationLinkByCode(String invitationCode, String platform);

    /**
     * 获取平台基础URL
     *
     * @param platform 平台类型
     * @return 基础URL
     */
    String getBaseUrlByPlatform(String platform);

}

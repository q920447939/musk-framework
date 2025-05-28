package org.example.musk.functions.invitation.service;

import org.example.musk.functions.invitation.service.dto.InvitationStatisticsDTO;

/**
 * 邀请统计服务接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationStatisticsService {

    /**
     * 获取会员邀请统计
     *
     * @param memberId 会员ID
     * @return 邀请统计信息
     */
    InvitationStatisticsDTO getMemberInvitationStatistics(Integer memberId);

    /**
     * 获取租户邀请统计
     *
     * @return 租户邀请统计信息
     */
    InvitationStatisticsDTO getTenantInvitationStatistics();

    /**
     * 刷新统计缓存
     *
     * @param memberId 会员ID（null表示刷新所有）
     */
    void refreshStatisticsCache(Integer memberId);

}

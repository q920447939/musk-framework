package org.example.musk.functions.invitation.service;

import org.example.musk.functions.invitation.dao.entity.MemberInvitationRewardRecordDO;
import org.example.musk.functions.member.invitation.entity.MemberInvitationEvent;

import java.util.List;

/**
 * 邀请奖励服务接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationRewardService {

    /**
     * 处理邀请奖励
     *
     * @param event 邀请事件
     */
    void processInvitationReward(MemberInvitationEvent event);

    /**
     * 发放奖励
     *
     * @param rewardRecordId 奖励记录ID
     * @return 是否发放成功
     */
    boolean grantReward(Long rewardRecordId);

    /**
     * 批量发放待发放的奖励
     *
     * @return 发放成功的数量
     */
    int batchGrantPendingRewards();

    /**
     * 重试发放失败的奖励
     *
     * @return 重试成功的数量
     */
    int retryFailedRewards();

    /**
     * 取消奖励
     *
     * @param rewardRecordId 奖励记录ID
     * @param reason 取消原因
     * @return 是否取消成功
     */
    boolean cancelReward(Long rewardRecordId, String reason);

    /**
     * 获取会员的奖励记录
     *
     * @param memberId 会员ID
     * @return 奖励记录列表
     */
    List<MemberInvitationRewardRecordDO> getMemberRewardRecords(Integer memberId);

    /**
     * 根据邀请关系获取奖励记录
     *
     * @param invitationRelationId 邀请关系ID
     * @return 奖励记录列表
     */
    List<MemberInvitationRewardRecordDO> getRewardRecordsByRelation(Long invitationRelationId);

}

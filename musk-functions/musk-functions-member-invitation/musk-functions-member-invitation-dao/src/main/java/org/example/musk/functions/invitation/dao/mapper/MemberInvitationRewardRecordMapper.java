package org.example.musk.functions.invitation.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRewardRecordDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

import java.util.List;

/**
 * 会员邀请奖励记录 Mapper
 *
 * @author musk-functions-member-invitation
 */
@Mapper
public interface MemberInvitationRewardRecordMapper extends BaseMapperX<MemberInvitationRewardRecordDO> {

    /**
     * 根据会员ID查询奖励记录
     *
     * @param rewardMemberId 获得奖励的会员ID
     * @return 奖励记录列表
     */
    default List<MemberInvitationRewardRecordDO> selectByRewardMemberId(Integer rewardMemberId) {
        return selectList(MemberInvitationRewardRecordDO::getRewardMemberId, rewardMemberId);
    }

    /**
     * 根据邀请关系ID查询奖励记录
     *
     * @param invitationRelationId 邀请关系ID
     * @return 奖励记录列表
     */
    default List<MemberInvitationRewardRecordDO> selectByInvitationRelationId(Long invitationRelationId) {
        return selectList(MemberInvitationRewardRecordDO::getInvitationRelationId, invitationRelationId);
    }

    /**
     * 根据规则ID查询奖励记录
     *
     * @param ruleId 规则ID
     * @return 奖励记录列表
     */
    default List<MemberInvitationRewardRecordDO> selectByRuleId(Long ruleId) {
        return selectList(MemberInvitationRewardRecordDO::getRuleId, ruleId);
    }

    /**
     * 查询待发放的奖励记录
     *
     * @return 奖励记录列表
     */
    default List<MemberInvitationRewardRecordDO> selectPendingRecords() {
        return selectList(new LambdaQueryWrapper<MemberInvitationRewardRecordDO>()
                .eq(MemberInvitationRewardRecordDO::getStatus, 1) // 待发放状态
                .orderByAsc(MemberInvitationRewardRecordDO::getCreateTime)
        );
    }

    /**
     * 查询发放失败的奖励记录
     *
     * @return 奖励记录列表
     */
    default List<MemberInvitationRewardRecordDO> selectFailedRecords() {
        return selectList(new LambdaQueryWrapper<MemberInvitationRewardRecordDO>()
                .eq(MemberInvitationRewardRecordDO::getStatus, 3) // 发放失败状态
                .orderByAsc(MemberInvitationRewardRecordDO::getCreateTime)
        );
    }

}

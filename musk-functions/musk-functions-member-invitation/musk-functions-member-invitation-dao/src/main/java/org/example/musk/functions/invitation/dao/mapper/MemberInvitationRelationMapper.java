package org.example.musk.functions.invitation.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRelationDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

import java.util.List;

/**
 * 会员邀请关系 Mapper
 *
 * @author musk-functions-member-invitation
 */
@Mapper
public interface MemberInvitationRelationMapper extends BaseMapperX<MemberInvitationRelationDO> {

    /**
     * 根据被邀请人查询邀请关系
     *
     * @param inviteeMemberId 被邀请人会员ID
     * @return 邀请关系
     */
    default MemberInvitationRelationDO selectByInviteeMemberId(Integer inviteeMemberId) {
        return selectOne(MemberInvitationRelationDO::getInviteeMemberId, inviteeMemberId);
    }

    /**
     * 根据邀请人查询邀请关系列表
     *
     * @param inviterMemberId 邀请人会员ID
     * @return 邀请关系列表
     */
    default List<MemberInvitationRelationDO> selectByInviterMemberId(Integer inviterMemberId) {
        return selectList(MemberInvitationRelationDO::getInviterMemberId, inviterMemberId);
    }

    /**
     * 根据邀请码查询邀请关系列表
     *
     * @param invitationCode 邀请码
     * @return 邀请关系列表
     */
    default List<MemberInvitationRelationDO> selectByInvitationCode(String invitationCode) {
        return selectList(MemberInvitationRelationDO::getInvitationCode, invitationCode);
    }

    /**
     * 统计邀请人的邀请数量
     *
     * @param inviterMemberId 邀请人会员ID
     * @return 邀请数量
     */
    default Long countByInviterMemberId(Integer inviterMemberId) {
        return selectCount(new LambdaQueryWrapper<MemberInvitationRelationDO>()
                .eq(MemberInvitationRelationDO::getInviterMemberId, inviterMemberId)
                .eq(MemberInvitationRelationDO::getStatus, 1) // 有效状态
        );
    }

    /**
     * 检查是否存在邀请关系
     *
     * @param inviterMemberId 邀请人会员ID
     * @param inviteeMemberId 被邀请人会员ID
     * @return 是否存在
     */
    default boolean existsRelation(Integer inviterMemberId, Integer inviteeMemberId) {
        return exists(new LambdaQueryWrapper<MemberInvitationRelationDO>()
                .eq(MemberInvitationRelationDO::getInviterMemberId, inviterMemberId)
                .eq(MemberInvitationRelationDO::getInviteeMemberId, inviteeMemberId)
        );
    }

}

package org.example.musk.functions.invitation.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationCodeDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员邀请码 Mapper
 *
 * @author musk-functions-member-invitation
 */
@Mapper
public interface MemberInvitationCodeMapper extends BaseMapperX<MemberInvitationCodeDO> {

    /**
     * 根据邀请码查询
     *
     * @param invitationCode 邀请码
     * @return 邀请码信息
     */
    default MemberInvitationCodeDO selectByInvitationCode(String invitationCode) {
        return selectOne(MemberInvitationCodeDO::getInvitationCode, invitationCode);
    }

    /**
     * 根据邀请人查询邀请码列表
     *
     * @param inviterMemberId 邀请人会员ID
     * @return 邀请码列表
     */
    default List<MemberInvitationCodeDO> selectByInviterMemberId(Integer inviterMemberId) {
        return selectList(MemberInvitationCodeDO::getInviterMemberId, inviterMemberId);
    }

    /**
     * 查询有效的邀请码
     *
     * @param invitationCode 邀请码
     * @return 邀请码信息
     */
    default MemberInvitationCodeDO selectValidCode(String invitationCode) {
        return selectOne(new LambdaQueryWrapper<MemberInvitationCodeDO>()
                .eq(MemberInvitationCodeDO::getInvitationCode, invitationCode)
                .eq(MemberInvitationCodeDO::getStatus, 1) // 有效状态
                .and(wrapper -> wrapper
                        .isNull(MemberInvitationCodeDO::getExpireTime)
                        .or()
                        .gt(MemberInvitationCodeDO::getExpireTime, LocalDateTime.now())
                )
        );
    }

    /**
     * 增加使用次数
     *
     * @param id 邀请码ID
     * @return 更新行数
     */
    default int incrementUsedCount(Long id) {
        MemberInvitationCodeDO updateEntity = new MemberInvitationCodeDO();
        updateEntity.setId(id);
        // 这里需要使用原生SQL或者在Service层处理
        return updateById(updateEntity);
    }

}

package org.example.musk.functions.invitation.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRewardRuleDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员邀请奖励规则 Mapper
 *
 * @author musk-functions-member-invitation
 */
@Mapper
public interface MemberInvitationRewardRuleMapper extends BaseMapperX<MemberInvitationRewardRuleDO> {

    /**
     * 根据规则编码查询
     *
     * @param ruleCode 规则编码
     * @return 奖励规则
     */
    default MemberInvitationRewardRuleDO selectByRuleCode(String ruleCode) {
        return selectOne(MemberInvitationRewardRuleDO::getRuleCode, ruleCode);
    }

    /**
     * 根据触发事件查询有效规则
     *
     * @param triggerEvent 触发事件
     * @return 奖励规则列表
     */
    default List<MemberInvitationRewardRuleDO> selectEffectiveRulesByEvent(String triggerEvent) {
        LocalDateTime now = LocalDateTime.now();
        return selectList(new LambdaQueryWrapper<MemberInvitationRewardRuleDO>()
                .eq(MemberInvitationRewardRuleDO::getTriggerEvent, triggerEvent)
                .eq(MemberInvitationRewardRuleDO::getStatus, 1) // 启用状态
                .and(wrapper -> wrapper
                        .isNull(MemberInvitationRewardRuleDO::getEffectiveStartTime)
                        .or()
                        .le(MemberInvitationRewardRuleDO::getEffectiveStartTime, now)
                )
                .and(wrapper -> wrapper
                        .isNull(MemberInvitationRewardRuleDO::getEffectiveEndTime)
                        .or()
                        .ge(MemberInvitationRewardRuleDO::getEffectiveEndTime, now)
                )
                .orderByDesc(MemberInvitationRewardRuleDO::getPriority)
        );
    }

    /**
     * 查询所有有效规则
     *
     * @return 奖励规则列表
     */
    default List<MemberInvitationRewardRuleDO> selectAllEffectiveRules() {
        LocalDateTime now = LocalDateTime.now();
        return selectList(new LambdaQueryWrapper<MemberInvitationRewardRuleDO>()
                .eq(MemberInvitationRewardRuleDO::getStatus, 1) // 启用状态
                .and(wrapper -> wrapper
                        .isNull(MemberInvitationRewardRuleDO::getEffectiveStartTime)
                        .or()
                        .le(MemberInvitationRewardRuleDO::getEffectiveStartTime, now)
                )
                .and(wrapper -> wrapper
                        .isNull(MemberInvitationRewardRuleDO::getEffectiveEndTime)
                        .or()
                        .ge(MemberInvitationRewardRuleDO::getEffectiveEndTime, now)
                )
                .orderByDesc(MemberInvitationRewardRuleDO::getPriority)
        );
    }

}

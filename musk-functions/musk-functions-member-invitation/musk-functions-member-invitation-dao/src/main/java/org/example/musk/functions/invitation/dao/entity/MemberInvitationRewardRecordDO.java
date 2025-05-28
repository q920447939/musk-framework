package org.example.musk.functions.invitation.dao.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员邀请奖励记录实体类
 *
 * @author musk-functions-member-invitation
 */
@TableName(value = "member_invitation_reward_record", autoResultMap = true)
@KeySequence("member_invitation_reward_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInvitationRewardRecordDO extends DomainBaseDO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 邀请关系ID
     */
    private Long invitationRelationId;

    /**
     * 获得奖励的会员ID
     */
    private Integer rewardMemberId;

    /**
     * 奖励类型
     */
    private String rewardType;

    /**
     * 奖励值
     */
    private BigDecimal rewardValue;

    /**
     * 触发事件
     */
    private String triggerEvent;

    /**
     * 触发源ID
     */
    private String triggerSourceId;

    /**
     * 发放状态(1:待发放 2:已发放 3:发放失败 4:已取消)
     */
    private Integer status;

    /**
     * 发放时间
     */
    private LocalDateTime grantTime;

    /**
     * 发放结果
     */
    private String grantResult;

}

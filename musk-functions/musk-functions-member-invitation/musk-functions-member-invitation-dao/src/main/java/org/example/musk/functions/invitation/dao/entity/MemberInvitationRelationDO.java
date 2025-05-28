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

import java.time.LocalDateTime;

/**
 * 会员邀请关系实体类
 *
 * @author musk-functions-member-invitation
 */
@TableName(value = "member_invitation_relation", autoResultMap = true)
@KeySequence("member_invitation_relation_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInvitationRelationDO extends DomainBaseDO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * 邀请人会员ID
     */
    private Integer inviterMemberId;

    /**
     * 被邀请人会员ID
     */
    private Integer inviteeMemberId;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 注册渠道
     */
    private String registerChannel;

    /**
     * 关系状态(1:有效 2:失效)
     */
    private Integer status;

}

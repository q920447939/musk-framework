package org.example.musk.functions.invitation.dao.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.musk.common.pojo.db.DomainBaseDO;

import java.time.LocalDateTime;

/**
 * 会员邀请码实体类
 *
 * @author musk-functions-member-invitation
 */
@TableName(value = "member_invitation_code", autoResultMap = true)
@KeySequence("member_invitation_code_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInvitationCodeDO extends DomainBaseDO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 邀请码(自动生成)
     */
    private String invitationCode;

    /**
     * 邀请人会员ID
     */
    private Integer inviterMemberId;

    /**
     * 邀请码类型(1:个人邀请码 2:活动邀请码)
     */
    private Integer codeType;

    /**
     * 状态(1:有效 2:失效 3:禁用)
     */
    private Integer status;

    /**
     * 最大使用次数(NULL表示无限制)
     */
    private Integer maxUseCount;

    /**
     * 已使用次数
     */
    private Integer usedCount;

    /**
     * 过期时间(NULL表示永不过期)
     */
    private LocalDateTime expireTime;

}

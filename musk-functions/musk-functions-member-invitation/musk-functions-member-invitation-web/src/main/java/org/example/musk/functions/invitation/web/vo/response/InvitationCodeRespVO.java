package org.example.musk.functions.invitation.web.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 邀请码响应VO
 *
 * @author musk-functions-member-invitation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationCodeRespVO {

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
     * 邀请码类型
     */
    private Integer codeType;

    /**
     * 邀请码类型名称
     */
    private String codeTypeName;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 最大使用次数
     */
    private Integer maxUseCount;

    /**
     * 已使用次数
     */
    private Integer usedCount;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

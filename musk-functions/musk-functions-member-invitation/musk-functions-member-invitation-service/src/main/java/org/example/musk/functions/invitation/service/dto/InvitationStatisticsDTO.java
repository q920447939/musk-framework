package org.example.musk.functions.invitation.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 邀请统计DTO
 *
 * @author musk-functions-member-invitation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationStatisticsDTO {

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 总邀请人数
     */
    private Long totalInvitations;

    /**
     * 有效邀请人数
     */
    private Long validInvitations;

    /**
     * 今日邀请人数
     */
    private Long todayInvitations;

    /**
     * 本月邀请人数
     */
    private Long monthInvitations;

    /**
     * 邀请转化率
     */
    private BigDecimal conversionRate;

    /**
     * 总奖励积分
     */
    private Integer totalRewardPoints;

    /**
     * 总奖励成长值
     */
    private Integer totalRewardGrowth;

    /**
     * 邀请码数量
     */
    private Long invitationCodeCount;

    /**
     * 有效邀请码数量
     */
    private Long validInvitationCodeCount;

}

package org.example.musk.functions.invitation.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.functions.invitation.service.InvitationStatisticsService;
import org.example.musk.functions.invitation.service.dto.InvitationStatisticsDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 邀请统计 Controller
 *
 * @author musk-functions-member-invitation
 */
@RestController
@RequestMapping("/api/invitation/statistics")
@Validated
@Slf4j
public class InvitationStatisticsController {

    @Resource
    private InvitationStatisticsService invitationStatisticsService;

    /**
     * 获取我的邀请统计
     *
     * @return 邀请统计信息
     */
    @GetMapping("/my")
    public CommonResult<InvitationStatisticsDTO> getMyInvitationStatistics() {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        InvitationStatisticsDTO statistics = invitationStatisticsService.getMemberInvitationStatistics(memberId);
        return CommonResult.success(statistics);
    }

    /**
     * 获取租户邀请统计（管理员接口）
     *
     * @return 租户邀请统计信息
     */
    @GetMapping("/tenant")
    public CommonResult<InvitationStatisticsDTO> getTenantInvitationStatistics() {
        InvitationStatisticsDTO statistics = invitationStatisticsService.getTenantInvitationStatistics();
        return CommonResult.success(statistics);
    }

    /**
     * 刷新统计缓存
     *
     * @return 操作结果
     */
    @PostMapping("/refresh")
    public CommonResult<Boolean> refreshStatisticsCache() {
        invitationStatisticsService.refreshStatisticsCache(null);
        return CommonResult.success(true);
    }

    /**
     * 刷新指定会员的统计缓存
     *
     * @param memberId 会员ID
     * @return 操作结果
     */
    @PostMapping("/refresh/{memberId}")
    public CommonResult<Boolean> refreshMemberStatisticsCache(@PathVariable Integer memberId) {
        invitationStatisticsService.refreshStatisticsCache(memberId);
        return CommonResult.success(true);
    }

}

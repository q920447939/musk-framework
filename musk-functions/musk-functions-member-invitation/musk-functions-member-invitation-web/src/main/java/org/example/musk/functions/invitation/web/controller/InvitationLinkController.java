package org.example.musk.functions.invitation.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.functions.invitation.service.InvitationLinkService;
import org.example.musk.functions.invitation.service.dto.InvitationLinkDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;

/**
 * 邀请链接 Controller
 *
 * @author musk-functions-member-invitation
 */
@RestController
@RequestMapping("/api/invitation/link")
@Validated
@Slf4j
public class InvitationLinkController {

    @Resource
    private InvitationLinkService invitationLinkService;

    /**
     * 生成邀请链接
     *
     * @param platform 平台类型（WEB、APP、H5）
     * @return 邀请链接信息
     */
    @PostMapping("/generate")
    public CommonResult<InvitationLinkDTO> generateInvitationLink(
            @RequestParam @NotBlank(message = "平台类型不能为空") String platform) {
        Integer memberId = ThreadLocalTenantContext.getMemberId();
        
        InvitationLinkDTO linkInfo = invitationLinkService.generateInvitationLink(memberId, platform);
        return CommonResult.success(linkInfo);
    }

    /**
     * 根据邀请码生成邀请链接
     *
     * @param invitationCode 邀请码
     * @param platform 平台类型
     * @return 邀请链接信息
     */
    @PostMapping("/generate/{invitationCode}")
    public CommonResult<InvitationLinkDTO> generateInvitationLinkByCode(
            @PathVariable @NotBlank(message = "邀请码不能为空") String invitationCode,
            @RequestParam @NotBlank(message = "平台类型不能为空") String platform) {
        
        InvitationLinkDTO linkInfo = invitationLinkService.generateInvitationLinkByCode(invitationCode, platform);
        return CommonResult.success(linkInfo);
    }

}

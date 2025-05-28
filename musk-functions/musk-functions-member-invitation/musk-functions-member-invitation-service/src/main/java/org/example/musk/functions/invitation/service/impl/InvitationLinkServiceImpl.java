package org.example.musk.functions.invitation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationCodeDO;
import org.example.musk.functions.invitation.dao.enums.InvitationCodeTypeEnum;
import org.example.musk.functions.invitation.service.InvitationCodeService;
import org.example.musk.functions.invitation.service.InvitationLinkService;
import org.example.musk.functions.invitation.service.QrCodeService;
import org.example.musk.functions.invitation.service.dto.InvitationLinkDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 邀请链接服务实现类
 *
 * @author musk-functions-member-invitation
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class InvitationLinkServiceImpl implements InvitationLinkService {

    @Resource
    private InvitationCodeService invitationCodeService;

    @Resource
    private QrCodeService qrCodeService;

    @Value("${musk.invitation.web.base-url:http://localhost:8080}")
    private String webBaseUrl;

    @Value("${musk.invitation.app.base-url:app://invitation}")
    private String appBaseUrl;

    @Value("${musk.invitation.h5.base-url:http://localhost:8080/h5}")
    private String h5BaseUrl;

    @Override
    public InvitationLinkDTO generateInvitationLink(Integer memberId, String platform) {
        if (memberId == null) {
            throw new BusinessException("会员ID不能为空");
        }
        if (StrUtil.isBlank(platform)) {
            throw new BusinessException("平台类型不能为空");
        }

        try {
            // 生成邀请码（个人邀请码，无使用次数限制，永不过期）
            MemberInvitationCodeDO codeEntity = invitationCodeService.generateInvitationCode(
                    memberId, InvitationCodeTypeEnum.PERSONAL, null, null);

            return generateInvitationLinkByCode(codeEntity.getInvitationCode(), platform);

        } catch (Exception e) {
            log.error("生成邀请链接失败，memberId={}, platform={}", memberId, platform, e);
            throw new BusinessException("生成邀请链接失败：" + e.getMessage());
        }
    }

    @Override
    public InvitationLinkDTO generateInvitationLinkByCode(String invitationCode, String platform) {
        if (StrUtil.isBlank(invitationCode)) {
            throw new BusinessException("邀请码不能为空");
        }
        if (StrUtil.isBlank(platform)) {
            throw new BusinessException("平台类型不能为空");
        }

        try {
            // 验证邀请码
            MemberInvitationCodeDO codeEntity = invitationCodeService.validateInvitationCode(invitationCode);
            if (codeEntity == null) {
                throw new BusinessException("邀请码无效或已失效");
            }

            // 获取平台基础URL
            String baseUrl = getBaseUrlByPlatform(platform);

            // 生成邀请链接
            String invitationLink = generateLink(baseUrl, invitationCode, platform);

            // 生成二维码
            String qrCodeUrl = qrCodeService.generateQrCode(invitationLink);

            log.info("生成邀请链接成功，invitationCode={}, platform={}, link={}",
                    invitationCode, platform, invitationLink);

            return InvitationLinkDTO.builder()
                    .invitationCode(invitationCode)
                    .invitationLink(invitationLink)
                    .qrCodeUrl(qrCodeUrl)
                    .platform(platform)
                    .build();

        } catch (Exception e) {
            log.error("生成邀请链接失败，invitationCode={}, platform={}", invitationCode, platform, e);
            throw new BusinessException("生成邀请链接失败：" + e.getMessage());
        }
    }

    @Override
    public String getBaseUrlByPlatform(String platform) {
        switch (platform.toUpperCase()) {
            case "WEB":
                return webBaseUrl;
            case "APP":
                return appBaseUrl;
            case "H5":
                return h5BaseUrl;
            default:
                log.warn("不支持的平台类型，使用默认Web URL，platform={}", platform);
                return webBaseUrl;
        }
    }

    /**
     * 生成链接
     *
     * @param baseUrl 基础URL
     * @param invitationCode 邀请码
     * @param platform 平台类型
     * @return 完整链接
     */
    private String generateLink(String baseUrl, String invitationCode, String platform) {
        if ("APP".equalsIgnoreCase(platform)) {
            // APP深度链接格式
            return String.format("%s?invitation_code=%s", baseUrl, invitationCode);
        } else {
            // Web/H5链接格式
            return String.format("%s/register?invitation_code=%s", baseUrl, invitationCode);
        }
    }

}

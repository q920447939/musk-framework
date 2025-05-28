package org.example.musk.functions.invitation.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邀请链接DTO
 *
 * @author musk-functions-member-invitation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationLinkDTO {

    /**
     * 邀请码
     */
    private String invitationCode;

    /**
     * 邀请链接
     */
    private String invitationLink;

    /**
     * 二维码URL
     */
    private String qrCodeUrl;

    /**
     * 平台类型
     */
    private String platform;

    /**
     * 短链接（可选）
     */
    private String shortLink;

}

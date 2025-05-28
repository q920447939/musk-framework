package org.example.musk.functions.invitation.service.generator;

import org.example.musk.functions.invitation.dao.enums.InvitationCodeTypeEnum;

/**
 * 邀请码生成器接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationCodeGenerator {

    /**
     * 生成邀请码
     *
     * @param memberId 会员ID
     * @param codeType 邀请码类型
     * @return 邀请码
     */
    String generateCode(Integer memberId, InvitationCodeTypeEnum codeType);

    /**
     * 验证邀请码格式
     *
     * @param invitationCode 邀请码
     * @return 是否有效
     */
    boolean validateCodeFormat(String invitationCode);

}

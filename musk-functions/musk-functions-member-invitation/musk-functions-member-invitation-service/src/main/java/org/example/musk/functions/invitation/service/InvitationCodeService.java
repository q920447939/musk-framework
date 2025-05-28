package org.example.musk.functions.invitation.service;

import org.example.musk.functions.invitation.dao.entity.MemberInvitationCodeDO;
import org.example.musk.functions.invitation.dao.enums.InvitationCodeTypeEnum;

import java.util.List;

/**
 * 邀请码服务接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationCodeService {

    /**
     * 生成邀请码
     *
     * @param memberId 会员ID
     * @param codeType 邀请码类型
     * @param maxUseCount 最大使用次数（null表示无限制）
     * @param expireHours 过期小时数（null表示永不过期）
     * @return 邀请码信息
     */
    MemberInvitationCodeDO generateInvitationCode(Integer memberId, InvitationCodeTypeEnum codeType, 
                                                  Integer maxUseCount, Integer expireHours);

    /**
     * 验证邀请码
     *
     * @param invitationCode 邀请码
     * @return 邀请码信息（null表示无效）
     */
    MemberInvitationCodeDO validateInvitationCode(String invitationCode);

    /**
     * 使用邀请码
     *
     * @param invitationCode 邀请码
     * @return 是否使用成功
     */
    boolean useInvitationCode(String invitationCode);

    /**
     * 获取会员的邀请码列表
     *
     * @param memberId 会员ID
     * @return 邀请码列表
     */
    List<MemberInvitationCodeDO> getMemberInvitationCodes(Integer memberId);

    /**
     * 禁用邀请码
     *
     * @param id 邀请码ID
     * @return 是否禁用成功
     */
    boolean disableInvitationCode(Long id);

    /**
     * 启用邀请码
     *
     * @param id 邀请码ID
     * @return 是否启用成功
     */
    boolean enableInvitationCode(Long id);

    /**
     * 根据ID获取邀请码
     *
     * @param id 邀请码ID
     * @return 邀请码信息
     */
    MemberInvitationCodeDO getInvitationCodeById(Long id);

    /**
     * 根据邀请码获取邀请码信息
     *
     * @param invitationCode 邀请码
     * @return 邀请码信息
     */
    MemberInvitationCodeDO getInvitationCodeByCode(String invitationCode);

}

package org.example.musk.functions.invitation.service;

/**
 * 邀请安全服务接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationSecurityService {

    /**
     * 检查注册安全性
     *
     * @param memberId 会员ID
     * @param clientIp 客户端IP
     * @param registerChannel 注册渠道
     * @return 是否通过安全检查
     */
    boolean checkRegistrationSecurity(Integer memberId, String clientIp, String registerChannel);

    /**
     * 检查IP限制
     *
     * @param clientIp 客户端IP
     * @return 是否通过IP限制检查
     */
    boolean checkIpLimit(String clientIp);

    /**
     * 检查设备指纹
     *
     * @param deviceId 设备ID
     * @return 是否通过设备指纹检查
     */
    boolean checkDeviceFingerprint(String deviceId);

    /**
     * 记录注册行为
     *
     * @param memberId 会员ID
     * @param clientIp 客户端IP
     * @param registerChannel 注册渠道
     */
    void recordRegistrationBehavior(Integer memberId, String clientIp, String registerChannel);

}

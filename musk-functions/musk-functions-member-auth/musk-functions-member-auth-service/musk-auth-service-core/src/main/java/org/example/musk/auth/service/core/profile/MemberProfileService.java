package org.example.musk.auth.service.core.profile;

import org.example.musk.auth.vo.req.profile.EmailManagementRequest;
import org.example.musk.auth.vo.req.profile.PhoneManagementRequest;
import org.example.musk.auth.vo.req.profile.UpdateBasicInfoRequest;
import org.example.musk.auth.vo.res.ContactManagementResponse;

/**
 * 会员信息管理服务接口
 *
 * @author musk
 */
public interface MemberProfileService {

    /**
     * 修改基础信息
     *
     * @param memberId 会员ID
     * @param request 修改请求
     * @return true-修改成功，false-修改失败
     */
    boolean updateBasicInfo(Integer memberId, UpdateBasicInfoRequest request);

    /**
     * 邮箱管理（绑定/解绑/更换）
     *
     * @param memberId 会员ID
     * @param request 邮箱管理请求
     * @return 管理结果
     */
    ContactManagementResponse manageEmail(Integer memberId, EmailManagementRequest request);

    /**
     * 手机号管理（绑定/解绑/更换）
     *
     * @param memberId 会员ID
     * @param request 手机号管理请求
     * @return 管理结果
     */
    ContactManagementResponse managePhone(Integer memberId, PhoneManagementRequest request);

    /**
     * 绑定邮箱
     *
     * @param memberId 会员ID
     * @param email 邮箱地址
     * @param verificationCode 验证码
     * @return true-绑定成功，false-绑定失败
     */
    boolean bindEmail(Integer memberId, String email, String verificationCode);

    /**
     * 解绑邮箱
     *
     * @param memberId 会员ID
     * @param email 邮箱地址
     * @param password 密码
     * @return true-解绑成功，false-解绑失败
     */
    boolean unbindEmail(Integer memberId, String email, String password);

    /**
     * 更换邮箱
     *
     * @param memberId 会员ID
     * @param currentEmail 当前邮箱
     * @param newEmail 新邮箱
     * @param verificationCode 验证码
     * @param password 密码
     * @return true-更换成功，false-更换失败
     */
    boolean changeEmail(Integer memberId, String currentEmail, String newEmail, 
                       String verificationCode, String password);

    /**
     * 绑定手机号
     *
     * @param memberId 会员ID
     * @param phone 手机号
     * @param verificationCode 验证码
     * @return true-绑定成功，false-绑定失败
     */
    boolean bindPhone(Integer memberId, String phone, String verificationCode);

    /**
     * 解绑手机号
     *
     * @param memberId 会员ID
     * @param phone 手机号
     * @param password 密码
     * @return true-解绑成功，false-解绑失败
     */
    boolean unbindPhone(Integer memberId, String phone, String password);

    /**
     * 更换手机号
     *
     * @param memberId 会员ID
     * @param currentPhone 当前手机号
     * @param newPhone 新手机号
     * @param verificationCode 验证码
     * @param password 密码
     * @return true-更换成功，false-更换失败
     */
    boolean changePhone(Integer memberId, String currentPhone, String newPhone, 
                       String verificationCode, String password);
}

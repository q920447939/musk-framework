package org.example.musk.auth.service.core.code;

import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.vo.req.code.SendCodeRequest;
import org.example.musk.auth.vo.req.code.VerifyCodeRequest;

/**
 * 验证码服务接口
 *
 * @author musk
 */
public interface VerificationCodeService {

    /**
     * 发送验证码
     *
     * @param request 发送验证码请求
     * @return true-发送成功，false-发送失败
     */
    boolean sendCode(SendCodeRequest request);

    /**
     * 验证验证码
     *
     * @param request 验证验证码请求
     * @return true-验证成功，false-验证失败
     */
    boolean verifyCode(VerifyCodeRequest request);

    /**
     * 使验证码失效
     *
     * @param target 目标（邮箱或手机号）
     * @param scene 验证码场景
     * @param channel 验证码渠道
     * @return true-操作成功，false-操作失败
     */
    boolean invalidateCode(String target, CodeSceneEnum scene, CodeChannelEnum channel);

    /**
     * 检查验证码发送频率限制
     *
     * @param target 目标（邮箱或手机号）
     * @param scene 验证码场景
     * @param channel 验证码渠道
     * @return true-可以发送，false-频率限制中
     */
    boolean checkSendFrequency(String target, CodeSceneEnum scene, CodeChannelEnum channel);

    /**
     * 获取验证码剩余有效时间（秒）
     *
     * @param target 目标（邮箱或手机号）
     * @param scene 验证码场景
     * @param channel 验证码渠道
     * @return 剩余有效时间，如果验证码不存在或已过期则返回0
     */
    long getCodeRemainingTime(String target, CodeSceneEnum scene, CodeChannelEnum channel);

    /**
     * 获取下次可发送验证码的剩余时间（秒）
     *
     * @param target 目标（邮箱或手机号）
     * @param scene 验证码场景
     * @param channel 验证码渠道
     * @return 剩余时间，如果可以立即发送则返回0
     */
    long getNextSendRemainingTime(String target, CodeSceneEnum scene, CodeChannelEnum channel);

    /**
     * 生成验证码
     * 默认生成6位数字验证码
     *
     * @return 验证码
     */
    default String generateCode() {
        return generateCode(6);
    }

    /**
     * 生成指定长度的验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    String generateCode(int length);

    /**
     * 构建验证码缓存键
     *
     * @param target 目标（邮箱或手机号）
     * @param scene 验证码场景
     * @param channel 验证码渠道
     * @return 缓存键
     */
    String buildCodeCacheKey(String target, CodeSceneEnum scene, CodeChannelEnum channel);

    /**
     * 构建发送频率限制缓存键
     *
     * @param target 目标（邮箱或手机号）
     * @param scene 验证码场景
     * @param channel 验证码渠道
     * @return 缓存键
     */
    String buildFrequencyCacheKey(String target, CodeSceneEnum scene, CodeChannelEnum channel);
}

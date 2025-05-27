package org.example.musk.auth.service.core.security.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.dao.security.MemberSecurityLogMapper;
import org.example.musk.auth.entity.security.MemberSecurityLogDO;
import org.example.musk.auth.enums.SecurityOperationEnum;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.enums.code.CodeChannelEnum;
import org.example.musk.auth.enums.code.CodeSceneEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.auth.service.core.code.CaptchaService;
import org.example.musk.auth.service.core.code.VerificationCodeService;
import org.example.musk.auth.service.core.config.AuthenticationConfig;
import org.example.musk.auth.service.core.security.MemberSecurityService;
import org.example.musk.auth.vo.req.security.SecurityLogQueryRequest;
import org.example.musk.auth.vo.req.code.VerifyCodeRequest;
import org.example.musk.auth.vo.req.security.SecurityVerificationRequest;
import org.example.musk.auth.vo.res.SecurityLogResponse;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.middleware.redis.RedisUtil;
import org.example.musk.plugin.service.dynamic.source.anno.PluginDynamicSource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 会员安全服务实现
 *
 * @author musk
 */
@Service
@Slf4j
@PluginDynamicSource(group = "auth", ds = "log")
public class MemberSecurityServiceImpl extends ServiceImpl<MemberSecurityLogMapper, MemberSecurityLogDO>
        implements MemberSecurityService {

    @Resource
    private MemberSecurityLogMapper securityLogMapper;

    @Resource
    private MemberAuthChannelService memberAuthChannelService;

    @Resource
    private CaptchaService captchaService;

    @Resource
    private VerificationCodeService verificationCodeService;

    @Resource
    private AuthenticationConfig authConfig;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 操作频率限制缓存键前缀
     */
    private static final String OPERATION_FREQUENCY_PREFIX = "operation_frequency:";

    @Override
    public boolean securityVerification(Integer memberId, SecurityVerificationRequest request) {
        log.info("[安全验证] 开始安全验证，memberId={}, operationType={}",
                memberId, request.getOperationType().getCode());

        try {
            SecurityOperationEnum operationType = request.getOperationType();

            // 1. 检查操作频率限制
            if (!checkOperationFrequencyLimit(memberId, operationType)) {
                log.warn("[安全验证] 操作频率超限，memberId={}, operationType={}",
                        memberId, operationType.getCode());
                return false;
            }

            // 2. 根据操作类型进行不同级别的验证
            boolean verificationResult = false;

            if (operationType.isHighSensitive()) {
                // 高敏感度操作：需要密码 + 验证码
                verificationResult = verifyHighSensitiveOperation(memberId, request);
            } else {
                // 中低敏感度操作：需要图形验证码
                verificationResult = verifyMediumSensitiveOperation(request);
            }

            // 3. 记录验证尝试
            recordOperationAttempt(memberId, operationType);

            log.info("[安全验证] 验证完成，memberId={}, operationType={}, result={}",
                    memberId, operationType.getCode(), verificationResult);

            return verificationResult;

        } catch (Exception e) {
            log.error("[安全验证] 验证异常，memberId={}, operationType={}",
                    memberId, request.getOperationType().getCode(), e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long recordSecurityLog(Integer memberId, SecurityOperationEnum operationType,
                                 String operationResult, String clientIp, String userAgent,
                                 String extraInfo, String failureReason) {
        log.debug("[安全日志] 记录安全操作日志，memberId={}, operationType={}, result={}",
                memberId, operationType.getCode(), operationResult);

        try {
            MemberSecurityLogDO securityLog = MemberSecurityLogDO.builder()
                    .memberId(memberId)
                    .operationType(operationType.getCode())
                    .operationResult(operationResult)
                    .clientIp(clientIp)
                    .userAgent(userAgent)
                    .extraInfo(extraInfo)
                    .failureReason(failureReason)
                    .operationDesc(operationType.getDesc())
                    .build();

            int result = securityLogMapper.insert(securityLog);
            if (result > 0) {
                log.debug("[安全日志] 记录成功，logId={}", securityLog.getId());
                return securityLog.getId();
            } else {
                log.warn("[安全日志] 记录失败，memberId={}", memberId);
                return null;
            }
        } catch (Exception e) {
            log.error("[安全日志] 记录异常，memberId={}", memberId, e);
            throw e;
        }
    }

    @Override
    public PageResult<SecurityLogResponse> querySecurityLogs(Integer memberId, SecurityLogQueryRequest request) {
        log.debug("[安全日志] 查询安全日志，memberId={}", memberId);

        try {
            // 构建查询条件
            LambdaQueryWrapper<MemberSecurityLogDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MemberSecurityLogDO::getMemberId, memberId);

            if (request.getOperationType() != null) {
                queryWrapper.eq(MemberSecurityLogDO::getOperationType, request.getOperationType().getCode());
            }

            if (StrUtil.isNotBlank(request.getOperationResult())) {
                queryWrapper.eq(MemberSecurityLogDO::getOperationResult, request.getOperationResult());
            }

            if (request.getStartTime() != null) {
                queryWrapper.ge(MemberSecurityLogDO::getCreateTime, request.getStartTime());
            }

            if (request.getEndTime() != null) {
                queryWrapper.le(MemberSecurityLogDO::getCreateTime, request.getEndTime());
            }

            if (StrUtil.isNotBlank(request.getClientIp())) {
                queryWrapper.eq(MemberSecurityLogDO::getClientIp, request.getClientIp());
            }

            queryWrapper.orderByDesc(MemberSecurityLogDO::getCreateTime);

            // 分页查询
            Page<MemberSecurityLogDO> page = new Page<>(request.getPageNum(), request.getPageSize());
            IPage<MemberSecurityLogDO> pageResult = securityLogMapper.selectPage(page, queryWrapper);

            // 转换为响应对象
            List<SecurityLogResponse> responseList = pageResult.getRecords().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            return new PageResult<>(responseList, pageResult.getTotal());

        } catch (Exception e) {
            log.error("[安全日志] 查询异常，memberId={}", memberId, e);
            throw e;
        }
    }

    @Override
    public boolean checkOperationFrequencyLimit(Integer memberId, SecurityOperationEnum operationType) {
        if (!authConfig.getSecurity().getEnableOperationFrequencyLimit()) {
            return true;
        }

        String cacheKey = OPERATION_FREQUENCY_PREFIX + memberId + ":" + operationType.getCode();
        String attempts = redisUtil.get(cacheKey);

        if (StrUtil.isBlank(attempts)) {
            return true;
        }

        int attemptCount = Integer.parseInt(attempts);
        int maxAttempts = authConfig.getSecurity().getSensitiveOperationMaxAttempts();

        return attemptCount < maxAttempts;
    }

    @Override
    public void recordOperationAttempt(Integer memberId, SecurityOperationEnum operationType) {
        if (!authConfig.getSecurity().getEnableOperationFrequencyLimit()) {
            return;
        }

        String cacheKey = OPERATION_FREQUENCY_PREFIX + memberId + ":" + operationType.getCode();
        String attempts = redisUtil.get(cacheKey);

        int attemptCount = StrUtil.isBlank(attempts) ? 0 : Integer.parseInt(attempts);
        attemptCount++;

        int windowMinutes = authConfig.getSecurity().getSensitiveOperationWindowMinutes();
        redisUtil.set(cacheKey, String.valueOf(attemptCount), windowMinutes, TimeUnit.MINUTES);
    }

    @Override
    public boolean verifyPassword(Integer memberId, String password) {
        // 这里需要实现密码验证逻辑
        // 可以通过MemberAuthChannelService验证
        return false; // 暂时返回false，需要后续完善
    }

    @Override
    public boolean verifyCaptcha(String sessionId, String code) {
        return captchaService.verifyCaptcha(sessionId, code);
    }

    @Override
    public boolean verifyEmailCode(String email, String code, String scene) {
        CodeSceneEnum codeScene = CodeSceneEnum.fromCode(scene);
        if (codeScene == null) {
            return false;
        }

        VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest();
        verifyCodeRequest.setTarget(email);
        verifyCodeRequest.setCode(code);
        verifyCodeRequest.setScene(codeScene);
        verifyCodeRequest.setChannel(CodeChannelEnum.EMAIL);

        return verificationCodeService.verifyCode(verifyCodeRequest);
    }

    @Override
    public boolean verifySmsCode(String phone, String code, String scene) {
        CodeSceneEnum codeScene = CodeSceneEnum.fromCode(scene);
        if (codeScene == null) {
            return false;
        }

        VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest();
        verifyCodeRequest.setTarget(phone);
        verifyCodeRequest.setCode(code);
        verifyCodeRequest.setScene(codeScene);
        verifyCodeRequest.setChannel(CodeChannelEnum.SMS);

        return verificationCodeService.verifyCode(verifyCodeRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanExpiredSecurityLogs(Integer retentionDays) {
        log.info("[安全日志] 清理过期安全日志，retentionDays={}", retentionDays);

        try {
            LocalDateTime expireTime = LocalDateTime.now().minusDays(retentionDays);

            LambdaQueryWrapper<MemberSecurityLogDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.lt(MemberSecurityLogDO::getCreateTime, expireTime);

            int deletedCount = securityLogMapper.delete(queryWrapper);
            log.info("[安全日志] 清理完成，deletedCount={}", deletedCount);

            return deletedCount;
        } catch (Exception e) {
            log.error("[安全日志] 清理异常", e);
            throw e;
        }
    }

    /**
     * 验证高敏感度操作
     *
     * @param memberId 会员ID
     * @param request 验证请求
     * @return 验证结果
     */
    private boolean verifyHighSensitiveOperation(Integer memberId, SecurityVerificationRequest request) {
        // 1. 验证密码
        if (StrUtil.isNotBlank(request.getPassword())) {
            if (!verifyPassword(memberId, request.getPassword())) {
                return false;
            }
        }

        // 2. 验证图形验证码
        if (StrUtil.isNotBlank(request.getCaptchaSessionId()) && StrUtil.isNotBlank(request.getCaptchaCode())) {
            if (!verifyCaptcha(request.getCaptchaSessionId(), request.getCaptchaCode())) {
                return false;
            }
        }

        // 3. 验证其他验证码（如果有）
        if (StrUtil.isNotBlank(request.getVerificationCode())) {
            // 这里需要根据具体场景验证邮箱或短信验证码
            // 暂时返回true，需要后续完善
        }

        return true;
    }

    /**
     * 验证中等敏感度操作
     *
     * @param request 验证请求
     * @return 验证结果
     */
    private boolean verifyMediumSensitiveOperation(SecurityVerificationRequest request) {
        // 验证图形验证码
        if (StrUtil.isNotBlank(request.getCaptchaSessionId()) && StrUtil.isNotBlank(request.getCaptchaCode())) {
            return verifyCaptcha(request.getCaptchaSessionId(), request.getCaptchaCode());
        }
        return true;
    }

    /**
     * 转换为响应对象
     *
     * @param securityLog 安全日志DO
     * @return 响应对象
     */
    private SecurityLogResponse convertToResponse(MemberSecurityLogDO securityLog) {
        SecurityLogResponse response = new SecurityLogResponse();
        BeanUtils.copyProperties(securityLog, response);

        response.setId(securityLog.getId());
        response.setOperationTime(securityLog.getCreateTime());
        response.setOperationResultDesc(getOperationResultDesc(securityLog.getOperationResult()));

        return response;
    }

    /**
     * 获取操作结果描述
     *
     * @param operationResult 操作结果
     * @return 结果描述
     */
    private String getOperationResultDesc(String operationResult) {
        switch (operationResult) {
            case "SUCCESS":
                return "成功";
            case "FAILED":
                return "失败";
            default:
                return "未知";
        }
    }
}

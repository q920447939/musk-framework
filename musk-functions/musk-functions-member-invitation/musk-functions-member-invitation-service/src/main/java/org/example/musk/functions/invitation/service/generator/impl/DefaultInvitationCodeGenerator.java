package org.example.musk.functions.invitation.service.generator.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.invitation.dao.enums.InvitationCodeTypeEnum;
import org.example.musk.functions.invitation.service.generator.InvitationCodeGenerator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 默认邀请码生成器实现
 * 
 * 生成规则：租户前缀(2位) + 时间戳(6位) + 随机数(4位) = 12位
 *
 * @author musk-functions-member-invitation
 */
@Component
@Slf4j
public class DefaultInvitationCodeGenerator implements InvitationCodeGenerator {

    /**
     * 邀请码长度
     */
    private static final int CODE_LENGTH = 12;

    /**
     * 租户前缀长度
     */
    private static final int TENANT_PREFIX_LENGTH = 2;

    /**
     * 时间戳长度
     */
    private static final int TIMESTAMP_LENGTH = 6;

    /**
     * 随机数长度
     */
    private static final int RANDOM_LENGTH = 4;

    /**
     * Base62字符集
     */
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 邀请码格式验证正则
     */
    private static final Pattern CODE_PATTERN = Pattern.compile("^[0-9A-Za-z]{12}$");

    @Override
    public String generateCode(Integer memberId, InvitationCodeTypeEnum codeType) {
        try {
            // 获取租户ID
            Integer tenantId = ThreadLocalTenantContext.getTenantId();
            if (tenantId == null) {
                throw new IllegalStateException("租户ID不能为空");
            }

            // 生成租户前缀（取租户ID的后两位，不足补0）
            String tenantPrefix = generateTenantPrefix(tenantId);

            // 生成时间戳部分（当前时间戳的后6位）
            String timestampPart = generateTimestampPart();

            // 生成随机数部分
            String randomPart = generateRandomPart();

            // 组合邀请码
            String invitationCode = tenantPrefix + timestampPart + randomPart;

            log.debug("生成邀请码成功，memberId={}, codeType={}, code={}", 
                    memberId, codeType.getDescription(), invitationCode);

            return invitationCode;

        } catch (Exception e) {
            log.error("生成邀请码失败，memberId={}, codeType={}", memberId, codeType, e);
            throw new RuntimeException("生成邀请码失败", e);
        }
    }

    @Override
    public boolean validateCodeFormat(String invitationCode) {
        if (StrUtil.isBlank(invitationCode)) {
            return false;
        }
        return CODE_PATTERN.matcher(invitationCode).matches();
    }

    /**
     * 生成租户前缀
     *
     * @param tenantId 租户ID
     * @return 租户前缀
     */
    private String generateTenantPrefix(Integer tenantId) {
        String tenantStr = String.valueOf(tenantId);
        if (tenantStr.length() >= TENANT_PREFIX_LENGTH) {
            return tenantStr.substring(tenantStr.length() - TENANT_PREFIX_LENGTH);
        } else {
            return StrUtil.padPre(tenantStr, TENANT_PREFIX_LENGTH, '0');
        }
    }

    /**
     * 生成时间戳部分
     *
     * @return 时间戳部分
     */
    private String generateTimestampPart() {
        long timestamp = System.currentTimeMillis();
        String timestampStr = toBase62(timestamp);
        if (timestampStr.length() >= TIMESTAMP_LENGTH) {
            return timestampStr.substring(timestampStr.length() - TIMESTAMP_LENGTH);
        } else {
            return StrUtil.padPre(timestampStr, TIMESTAMP_LENGTH, '0');
        }
    }

    /**
     * 生成随机数部分
     *
     * @return 随机数部分
     */
    private String generateRandomPart() {
        return RandomUtil.randomString(BASE62_CHARS, RANDOM_LENGTH);
    }

    /**
     * 转换为Base62编码
     *
     * @param number 数字
     * @return Base62字符串
     */
    private String toBase62(long number) {
        if (number == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();
        while (number > 0) {
            result.insert(0, BASE62_CHARS.charAt((int) (number % 62)));
            number /= 62;
        }
        return result.toString();
    }

}

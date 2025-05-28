package org.example.musk.functions.invitation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.invitation.service.InvitationSecurityService;
import org.example.musk.functions.invitation.service.config.InvitationConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 邀请安全服务实现类
 *
 * @author musk-functions-member-invitation
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class InvitationSecurityServiceImpl implements InvitationSecurityService {

    @Resource
    private InvitationConfig invitationConfig;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String IP_LIMIT_KEY_PREFIX = "invitation:ip_limit:";
    private static final String DEVICE_LIMIT_KEY_PREFIX = "invitation:device_limit:";
    private static final String REGISTRATION_RECORD_KEY_PREFIX = "invitation:registration:";

    @Override
    public boolean checkRegistrationSecurity(Integer memberId, String clientIp, String registerChannel) {
        try {
            // 检查IP限制
            if (invitationConfig.getSecurity().getEnableIpLimit() && !checkIpLimit(clientIp)) {
                log.warn("IP限制检查失败，memberId={}, clientIp={}", memberId, clientIp);
                return false;
            }

            // 记录注册行为
            recordRegistrationBehavior(memberId, clientIp, registerChannel);

            return true;

        } catch (Exception e) {
            log.error("安全检查异常，memberId={}, clientIp={}", memberId, clientIp, e);
            // 异常情况下，为了安全起见，拒绝注册
            return false;
        }
    }

    @Override
    public boolean checkIpLimit(String clientIp) {
        if (StrUtil.isBlank(clientIp)) {
            log.warn("客户端IP为空，拒绝注册");
            return false;
        }

        try {
            Integer tenantId = ThreadLocalTenantContext.getTenantId();
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String key = IP_LIMIT_KEY_PREFIX + tenantId + ":" + clientIp + ":" + today;

            // 获取当前IP今日注册次数
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
            if (count == null) {
                count = 0;
            }

            // 检查是否超过限制
            Integer maxRegistrations = invitationConfig.getSecurity().getMaxRegistrationsPerIpPerDay();
            if (count >= maxRegistrations) {
                log.warn("IP注册次数超过限制，clientIp={}, count={}, limit={}", clientIp, count, maxRegistrations);
                return false;
            }

            // 增加计数
            redisTemplate.opsForValue().increment(key);
            // 设置过期时间为明天凌晨
            redisTemplate.expire(key, 24, TimeUnit.HOURS);

            log.debug("IP限制检查通过，clientIp={}, count={}", clientIp, count + 1);
            return true;

        } catch (Exception e) {
            log.error("IP限制检查异常，clientIp={}", clientIp, e);
            return false;
        }
    }

    @Override
    public boolean checkDeviceFingerprint(String deviceId) {
        if (!invitationConfig.getSecurity().getEnableDeviceFingerprint()) {
            return true;
        }

        if (StrUtil.isBlank(deviceId)) {
            log.warn("设备ID为空，跳过设备指纹检查");
            return true;
        }

        try {
            Integer tenantId = ThreadLocalTenantContext.getTenantId();
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String key = DEVICE_LIMIT_KEY_PREFIX + tenantId + ":" + deviceId + ":" + today;

            // 检查设备今日是否已注册
            Boolean exists = redisTemplate.hasKey(key);
            if (Boolean.TRUE.equals(exists)) {
                log.warn("设备今日已注册，deviceId={}", deviceId);
                return false;
            }

            // 标记设备已注册
            redisTemplate.opsForValue().set(key, "1", 24, TimeUnit.HOURS);

            log.debug("设备指纹检查通过，deviceId={}", deviceId);
            return true;

        } catch (Exception e) {
            log.error("设备指纹检查异常，deviceId={}", deviceId, e);
            return false;
        }
    }

    @Override
    public void recordRegistrationBehavior(Integer memberId, String clientIp, String registerChannel) {
        try {
            Integer tenantId = ThreadLocalTenantContext.getTenantId();
            String key = REGISTRATION_RECORD_KEY_PREFIX + tenantId + ":" + memberId;

            // 记录注册信息
            String registrationInfo = String.format("ip:%s,channel:%s,time:%s",
                    clientIp, registerChannel, System.currentTimeMillis());

            redisTemplate.opsForValue().set(key, registrationInfo, 30, TimeUnit.DAYS);

            log.debug("记录注册行为，memberId={}, clientIp={}, registerChannel={}",
                    memberId, clientIp, registerChannel);

        } catch (Exception e) {
            log.error("记录注册行为异常，memberId={}, clientIp={}", memberId, clientIp, e);
        }
    }

}

package org.example.musk.auth.service.core.channel.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.dao.channel.MemberAuthChannelMapper;
import org.example.musk.auth.dao.member.MemberMapper;
import org.example.musk.auth.entity.channel.MemberAuthChannelDO;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;
import org.example.musk.auth.service.core.channel.MemberAuthChannelService;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员认证渠道服务实现
 *
 * 使用 MyBatis-Plus ORM 风格，通过 LambdaQueryWrapper/LambdaUpdateWrapper
 * 动态生成 SQL，无需自定义 mapper 方法
 *
 * @author musk
 */
@Service
@Slf4j
public class MemberAuthChannelServiceImpl implements MemberAuthChannelService {

    @Resource
    private MemberAuthChannelMapper memberAuthChannelMapper;

    @Resource
    private MemberMapper memberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addAuthChannel(Integer memberId, AuthChannelTypeEnum channelType,
                              String identifier, String value, boolean isVerified) {
        return addAuthChannel(memberId, channelType, identifier, value, isVerified, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addAuthChannel(Integer memberId, AuthChannelTypeEnum channelType,
                              String identifier, String value, boolean isVerified, String extraInfo) {
        log.info("[添加认证渠道] 开始添加认证渠道，memberId={}, channelType={}, identifier={}",
                memberId, channelType.getCode(), identifier);

        // 参数校验
        if (memberId == null || StrUtil.isBlank(identifier)) {
            throw new BusinessException(BusinessPageExceptionEnum.PARAMS_ERROR);
        }

        // 检查会员是否存在
        MemberDO member = memberMapper.selectById(memberId);
        if (member == null) {
            log.warn("[添加认证渠道] 会员不存在，memberId={}", memberId);
            throw new BusinessException(BusinessPageExceptionEnum.MEMBER_NOT_EXISTS);
        }

        // 检查渠道标识是否已被使用
        if (isChannelIdentifierUsed(channelType, identifier)) {
            log.warn("[添加认证渠道] 渠道标识已被使用，channelType={}, identifier={}",
                    channelType.getCode(), identifier);
            throw new BusinessException(BusinessPageExceptionEnum.AUTH_CHANNEL_ALREADY_EXISTS);
        }

        // 构建认证渠道实体
        MemberAuthChannelDO authChannel = MemberAuthChannelDO.builder()
                .memberId(memberId)
                .channelType(channelType.getCode())
                .channelIdentifier(identifier)
                .channelValue(value)
                .isVerified(isVerified)
                .isPrimary(false) // 默认不是主要认证方式
                .verifiedTime(isVerified ? LocalDateTime.now() : null)
                .extraInfo(extraInfo)
                .tenantId(ThreadLocalTenantContext.getTenantId())
                .domainId(ThreadLocalTenantContext.getDomainId())
                .build();

        // 插入数据库
        int result = memberAuthChannelMapper.insert(authChannel);
        if (result <= 0) {
            log.error("[添加认证渠道] 插入数据库失败，memberId={}, channelType={}",
                    memberId, channelType.getCode());
            throw new BusinessException(BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE);
        }

        log.info("[添加认证渠道] 添加认证渠道成功，id={}, memberId={}, channelType={}",
                authChannel.getId(), memberId, channelType.getCode());
        return authChannel.getId();
    }

    @Override
    public boolean verifyAuthChannel(AuthChannelTypeEnum channelType, String identifier, String value) {
        log.debug("[验证认证渠道] 开始验证，channelType={}, identifier={}", channelType.getCode(), identifier);

        MemberAuthChannelDO authChannel = findAuthChannel(channelType, identifier);
        if (authChannel == null) {
            log.debug("[验证认证渠道] 认证渠道不存在，channelType={}, identifier={}",
                    channelType.getCode(), identifier);
            return false;
        }

        // 根据渠道类型进行不同的验证逻辑
        boolean verified = false;
        switch (channelType) {
            case USERNAME:
                // 用户名密码验证：比较加密后的密码
                verified = StrUtil.equals(value, authChannel.getChannelValue());
                break;
            case EMAIL:
            case PHONE:
                // 邮箱/手机号验证：直接比较标识符
                verified = StrUtil.equals(identifier, authChannel.getChannelIdentifier());
                break;
            default:
                // 第三方登录验证：比较openId等
                verified = StrUtil.equals(value, authChannel.getChannelValue());
                break;
        }

        if (verified) {
            // 更新最后使用时间
            updateLastUsedTime(channelType, identifier);
        }

        log.debug("[验证认证渠道] 验证结果={}, channelType={}, identifier={}",
                verified, channelType.getCode(), identifier);
        return verified;
    }

    @Override
    public MemberDO findMemberByChannel(AuthChannelTypeEnum channelType, String identifier) {
        MemberAuthChannelDO authChannel = findAuthChannel(channelType, identifier);
        if (authChannel == null) {
            return null;
        }

        return memberMapper.selectById(authChannel.getMemberId());
    }

    @Override
    public MemberAuthChannelDO findAuthChannel(AuthChannelTypeEnum channelType, String identifier) {
        if (channelType == null || StrUtil.isBlank(identifier)) {
            return null;
        }
        return memberAuthChannelMapper.selectOne(
                new LambdaQueryWrapper<MemberAuthChannelDO>()
                        .eq(MemberAuthChannelDO::getChannelType, channelType.getCode())
                        .eq(MemberAuthChannelDO::getChannelIdentifier, identifier)
                        .last("LIMIT 1")
        );
    }

    @Override
    public List<MemberAuthChannelDO> getMemberAuthChannels(Integer memberId) {
        if (memberId == null) {
            return List.of();
        }

        return memberAuthChannelMapper.selectList(
                new LambdaQueryWrapper<MemberAuthChannelDO>()
                        .eq(MemberAuthChannelDO::getMemberId, memberId)
        );
    }

    @Override
    public List<MemberAuthChannelDO> getMemberAuthChannels(Integer memberId, AuthChannelTypeEnum channelType) {
        if (memberId == null || channelType == null) {
            return List.of();
        }

        return memberAuthChannelMapper.selectList(
                new LambdaQueryWrapper<MemberAuthChannelDO>()
                        .eq(MemberAuthChannelDO::getMemberId, memberId)
                        .eq(MemberAuthChannelDO::getChannelType, channelType.getCode())
        );
    }

    @Override
    public MemberAuthChannelDO getPrimaryAuthChannel(Integer memberId) {
        if (memberId == null) {
            return null;
        }

        return memberAuthChannelMapper.selectOne(
                new LambdaQueryWrapper<MemberAuthChannelDO>()
                        .eq(MemberAuthChannelDO::getMemberId, memberId)
                        .eq(MemberAuthChannelDO::getIsPrimary, true)
                        .last("LIMIT 1")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setPrimaryAuthChannel(Integer memberId, AuthChannelTypeEnum channelType, String identifier) {
        log.info("[设置主要认证方式] 开始设置，memberId={}, channelType={}, identifier={}",
                memberId, channelType.getCode(), identifier);

        // 查找目标认证渠道
        MemberAuthChannelDO targetChannel = findAuthChannel(channelType, identifier);
        if (targetChannel == null || !targetChannel.getMemberId().equals(memberId)) {
            log.warn("[设置主要认证方式] 认证渠道不存在或不属于该会员，memberId={}, channelType={}, identifier={}",
                    memberId, channelType.getCode(), identifier);
            return false;
        }

        try {
            // 1. 取消该会员的所有主要认证渠道
            memberAuthChannelMapper.update(
                    null,
                    new LambdaUpdateWrapper<MemberAuthChannelDO>()
                            .eq(MemberAuthChannelDO::getMemberId, memberId)
                            .eq(MemberAuthChannelDO::getIsPrimary, true)
                            .set(MemberAuthChannelDO::getIsPrimary, false)
                            .set(MemberAuthChannelDO::getUpdateTime, LocalDateTime.now())
            );

            // 2. 设置新的主要认证渠道
            int result = memberAuthChannelMapper.update(
                    null,
                    new LambdaUpdateWrapper<MemberAuthChannelDO>()
                            .eq(MemberAuthChannelDO::getId, targetChannel.getId())
                            .set(MemberAuthChannelDO::getIsPrimary, true)
                            .set(MemberAuthChannelDO::getUpdateTime, LocalDateTime.now())
            );

            log.info("[设置主要认证方式] 设置成功，memberId={}, channelType={}, identifier={}",
                    memberId, channelType.getCode(), identifier);
            return result > 0;
        } catch (Exception e) {
            log.error("[设置主要认证方式] 设置失败，memberId={}, channelType={}, identifier={}",
                    memberId, channelType.getCode(), identifier, e);
            throw new BusinessException(BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindAuthChannel(Integer memberId, AuthChannelTypeEnum channelType, String identifier) {
        log.info("[解绑认证渠道] 开始解绑，memberId={}, channelType={}, identifier={}",
                memberId, channelType.getCode(), identifier);

        // 查找认证渠道
        MemberAuthChannelDO authChannel = findAuthChannel(channelType, identifier);
        if (authChannel == null || !authChannel.getMemberId().equals(memberId)) {
            log.warn("[解绑认证渠道] 认证渠道不存在或不属于该会员，memberId={}, channelType={}, identifier={}",
                    memberId, channelType.getCode(), identifier);
            return false;
        }

        // 检查是否为最后一个认证渠道
        List<MemberAuthChannelDO> memberChannels = getMemberAuthChannels(memberId);
        if (memberChannels.size() <= 1) {
            log.warn("[解绑认证渠道] 不能解绑最后一个认证渠道，memberId={}", memberId);
            throw new BusinessException(BusinessPageExceptionEnum.CANNOT_UNBIND_LAST_AUTH_CHANNEL);
        }

        try {
            // 逻辑删除认证渠道
            LambdaUpdateWrapper<MemberAuthChannelDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MemberAuthChannelDO::getId, authChannel.getId())
                    .set(MemberAuthChannelDO::getDeleted, true)
                    .set(MemberAuthChannelDO::getUpdateTime, LocalDateTime.now());

            int result = memberAuthChannelMapper.update(null, updateWrapper);

            // 如果解绑的是主要认证渠道，需要重新设置主要认证渠道
            if (authChannel.getIsPrimary()) {
                setNewPrimaryChannel(memberId);
            }

            log.info("[解绑认证渠道] 解绑成功，memberId={}, channelType={}, identifier={}",
                    memberId, channelType.getCode(), identifier);
            return result > 0;
        } catch (Exception e) {
            log.error("[解绑认证渠道] 解绑失败，memberId={}, channelType={}, identifier={}",
                    memberId, channelType.getCode(), identifier, e);
            throw new BusinessException(BusinessPageExceptionEnum.COMMON_RESULT_BOOL_IS_FALSE);
        }
    }

    @Override
    public boolean updateVerifiedStatus(AuthChannelTypeEnum channelType, String identifier, boolean isVerified) {
        MemberAuthChannelDO authChannel = findAuthChannel(channelType, identifier);
        if (authChannel == null) {
            return false;
        }

        LocalDateTime verifiedTime = isVerified ? LocalDateTime.now() : null;
        int result = memberAuthChannelMapper.update(
                null,
                new LambdaUpdateWrapper<MemberAuthChannelDO>()
                        .eq(MemberAuthChannelDO::getId, authChannel.getId())
                        .set(MemberAuthChannelDO::getIsVerified, isVerified)
                        .set(MemberAuthChannelDO::getVerifiedTime, verifiedTime)
                        .set(MemberAuthChannelDO::getUpdateTime, LocalDateTime.now())
        );

        log.info("[更新验证状态] 更新成功，channelType={}, identifier={}, isVerified={}",
                channelType.getCode(), identifier, isVerified);
        return result > 0;
    }

    @Override
    public boolean updateLastUsedTime(AuthChannelTypeEnum channelType, String identifier) {
        MemberAuthChannelDO authChannel = findAuthChannel(channelType, identifier);
        if (authChannel == null) {
            return false;
        }

        int result = memberAuthChannelMapper.update(
                null,
                new LambdaUpdateWrapper<MemberAuthChannelDO>()
                        .eq(MemberAuthChannelDO::getId, authChannel.getId())
                        .set(MemberAuthChannelDO::getLastUsedTime, LocalDateTime.now())
                        .set(MemberAuthChannelDO::getUpdateTime, LocalDateTime.now())
        );
        return result > 0;
    }

    @Override
    public boolean updateChannelValue(AuthChannelTypeEnum channelType, String identifier, String newValue) {
        log.info("[更新认证渠道值] 开始更新，channelType={}, identifier={}", channelType.getCode(), identifier);

        MemberAuthChannelDO authChannel = findAuthChannel(channelType, identifier);
        if (authChannel == null) {
            log.warn("[更新认证渠道值] 认证渠道不存在，channelType={}, identifier={}",
                    channelType.getCode(), identifier);
            return false;
        }

        try {
            LambdaUpdateWrapper<MemberAuthChannelDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(MemberAuthChannelDO::getId, authChannel.getId())
                    .set(MemberAuthChannelDO::getChannelValue, newValue)
                    .set(MemberAuthChannelDO::getUpdateTime, LocalDateTime.now());

            int result = memberAuthChannelMapper.update(null, updateWrapper);

            log.info("[更新认证渠道值] 更新成功，channelType={}, identifier={}",
                    channelType.getCode(), identifier);
            return result > 0;
        } catch (Exception e) {
            log.error("[更新认证渠道值] 更新失败，channelType={}, identifier={}",
                    channelType.getCode(), identifier, e);
            return false;
        }
    }

    @Override
    public boolean isChannelBound(Integer memberId, AuthChannelTypeEnum channelType, String identifier) {
        if (memberId == null || channelType == null || StrUtil.isBlank(identifier)) {
            return false;
        }

        MemberAuthChannelDO authChannel = findAuthChannel(channelType, identifier);
        return authChannel != null && authChannel.getMemberId().equals(memberId);
    }

    @Override
    public boolean isChannelIdentifierUsed(AuthChannelTypeEnum channelType, String identifier) {
        return findAuthChannel(channelType, identifier) != null;
    }

    /**
     * 设置新的主要认证渠道
     * 当原主要认证渠道被解绑时调用
     *
     * @param memberId 会员ID
     */
    private void setNewPrimaryChannel(Integer memberId) {
        List<MemberAuthChannelDO> channels = getMemberAuthChannels(memberId);
        if (!channels.isEmpty()) {
            // 选择第一个认证渠道作为新的主要认证渠道
            MemberAuthChannelDO firstChannel = channels.getFirst();
            memberAuthChannelMapper.update(
                    null,
                    new LambdaUpdateWrapper<MemberAuthChannelDO>()
                            .eq(MemberAuthChannelDO::getId, firstChannel.getId())
                            .set(MemberAuthChannelDO::getIsPrimary, true)
                            .set(MemberAuthChannelDO::getUpdateTime, LocalDateTime.now())
            );

            log.info("[设置新的主要认证渠道] 设置成功，memberId={}, channelId={}, channelType={}",
                    memberId, firstChannel.getId(), firstChannel.getChannelType());
        }
    }
}

package org.example.musk.tests.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.functions.message.dao.SystemMessageMapper;
import org.example.musk.functions.message.dao.SystemUserMessageMapper;
import org.example.musk.functions.message.exception.MessageException;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.entity.SystemUserMessageDO;
import org.example.musk.tests.service.MessageReadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息读取服务实现类
 *
 * @author musk-functions-message
 */
@Service
@Validated
@Slf4j
@DS(MessageConstant.DB_NAME)
public class MessageReadServiceImpl implements MessageReadService {

    @Resource
    private SystemMessageMapper systemMessageMapper;

    @Resource
    private SystemUserMessageMapper systemUserMessageMapper;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'unread:' + #userId + ':*'")
    public boolean markAsRead(Integer messageId, Integer userId) {
        // 校验消息存在
        SystemMessageDO message = systemMessageMapper.selectById(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        // 查询用户消息关联
        SystemUserMessageDO userMessage = getUserMessage(messageId, userId);

        // 如果用户消息关联不存在，则创建
        if (userMessage == null) {
            userMessage = createUserMessage(messageId, userId, message.getTenantId(), message.getDomainId());
        }

        // 如果已经是已读状态，则直接返回成功
        if (Boolean.TRUE.equals(userMessage.getIsRead())) {
            return true;
        }

        // 更新为已读状态
        LambdaUpdateWrapper<SystemUserMessageDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemUserMessageDO::getId, userMessage.getId())
                .set(SystemUserMessageDO::getIsRead, true)
                .set(SystemUserMessageDO::getReadTime, LocalDateTime.now());

        int rows = systemUserMessageMapper.update(null, updateWrapper);

        if (rows > 0) {
            log.info("[markAsRead][标记消息为已读成功：messageId={}, userId={}]", messageId, userId);
            return true;
        }

        return false;
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'unread:' + #userId + ':*'")
    public int markAllAsRead(Integer userId, Integer tenantId, Integer domainId) {
        // 查询用户未读消息
        LambdaQueryWrapper<SystemUserMessageDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUserMessageDO::getUserId, userId)
                .eq(SystemUserMessageDO::getTenantId, tenantId)
                .eq(SystemUserMessageDO::getDomainId, domainId)
                .eq(SystemUserMessageDO::getIsRead, false)
                .eq(SystemUserMessageDO::getIsDeletedByUser, false);

        List<SystemUserMessageDO> userMessages = systemUserMessageMapper.selectList(queryWrapper);

        if (userMessages.isEmpty()) {
            return 0;
        }

        // 批量更新为已读状态
        LocalDateTime now = LocalDateTime.now();
        int count = 0;

        for (SystemUserMessageDO userMessage : userMessages) {
            LambdaUpdateWrapper<SystemUserMessageDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SystemUserMessageDO::getId, userMessage.getId())
                    .set(SystemUserMessageDO::getIsRead, true)
                    .set(SystemUserMessageDO::getReadTime, now);

            count += systemUserMessageMapper.update(null, updateWrapper);
        }

        log.info("[markAllAsRead][标记所有消息为已读成功：userId={}, count={}]", userId, count);
        return count;
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'unread:' + #userId + ':*'")
    public boolean deleteUserMessage(Integer messageId, Integer userId) {
        // 校验消息存在
        SystemMessageDO message = systemMessageMapper.selectById(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        // 查询用户消息关联
        SystemUserMessageDO userMessage = getUserMessage(messageId, userId);

        // 如果用户消息关联不存在，则创建
        if (userMessage == null) {
            userMessage = createUserMessage(messageId, userId, message.getTenantId(), message.getDomainId());
        }

        // 如果已经是删除状态，则直接返回成功
        if (Boolean.TRUE.equals(userMessage.getIsDeletedByUser())) {
            return true;
        }

        // 更新为删除状态
        LambdaUpdateWrapper<SystemUserMessageDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemUserMessageDO::getId, userMessage.getId())
                .set(SystemUserMessageDO::getIsDeletedByUser, true);

        int rows = systemUserMessageMapper.update(null, updateWrapper);

        if (rows > 0) {
            log.info("[deleteUserMessage][用户删除消息成功：messageId={}, userId={}]", messageId, userId);
            return true;
        }

        return false;
    }

    /**
     * 获取用户消息关联
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 用户消息关联
     */
    private SystemUserMessageDO getUserMessage(Integer messageId, Integer userId) {
        LambdaQueryWrapper<SystemUserMessageDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUserMessageDO::getMessageId, messageId)
                .eq(SystemUserMessageDO::getUserId, userId);

        return systemUserMessageMapper.selectOne(queryWrapper);
    }

    /**
     * 创建用户消息关联
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 用户消息关联
     */
    private SystemUserMessageDO createUserMessage(Integer messageId, Integer userId, Integer tenantId, Integer domainId) {
        SystemUserMessageDO userMessage = new SystemUserMessageDO();
        userMessage.setMessageId(messageId);
        userMessage.setUserId(userId);
        userMessage.setTenantId(tenantId);
        userMessage.setDomainId(domainId);
        userMessage.setIsRead(false);
        userMessage.setIsDeletedByUser(false);

        systemUserMessageMapper.insert(userMessage);

        return userMessage;
    }
}

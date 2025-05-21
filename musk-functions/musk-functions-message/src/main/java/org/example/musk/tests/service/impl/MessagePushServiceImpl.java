package org.example.musk.tests.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.functions.message.dao.SystemMessageMapper;
import org.example.musk.functions.message.dao.SystemMessageSendRecordMapper;
import org.example.musk.functions.message.dao.SystemUserMessageMapper;
import org.example.musk.functions.message.enums.MessageSendStatusEnum;
import org.example.musk.functions.message.exception.MessageException;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.entity.SystemMessageSendRecordDO;
import org.example.musk.functions.message.model.entity.SystemUserMessageDO;
import org.example.musk.functions.message.mq.MessagePushMessage;
import org.example.musk.tests.service.MessagePushService;
import org.example.musk.middleware.mq.redis.core.RedisMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * 消息推送服务实现类
 *
 * @author musk-functions-message
 */
@Service
@Validated
@Slf4j
@DS(MessageConstant.DB_NAME)
public class MessagePushServiceImpl implements MessagePushService {

    @Resource
    private SystemMessageMapper systemMessageMapper;

    @Resource
    private SystemUserMessageMapper systemUserMessageMapper;

    @Resource
    private SystemMessageSendRecordMapper systemMessageSendRecordMapper;

    @Resource
    private RedisMQTemplate redisMQTemplate;

    @Override
    public boolean pushMessage(Integer messageId, Integer userId) {
        try {
            // 创建消息推送记录
            SystemMessageSendRecordDO record = createSendRecord(messageId, userId);

            // 构建消息推送对象
            MessagePushMessage pushMessage = new MessagePushMessage()
                    .setMessageId(messageId)
                    .setUserId(userId)
                    .setSendRecordId(record.getId());

            // 发送消息到Redis Stream
            redisMQTemplate.send(pushMessage);

            // 更新发送状态为发送中
            updateSendStatus(record.getId(), MessageSendStatusEnum.SENDING.getStatus(), null);

            log.info("[pushMessage][推送消息成功：messageId={}, userId={}]", messageId, userId);
            return true;
        } catch (Exception e) {
            log.error("[pushMessage][推送消息异常：messageId={}, userId={}]", messageId, userId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processMessagePush(Integer messageId, Integer userId, Integer sendRecordId) {
        try {
            // 获取消息
            SystemMessageDO message = systemMessageMapper.selectById(messageId);
            if (message == null) {
                updateSendStatus(sendRecordId, MessageSendStatusEnum.FAILED.getStatus(), "消息不存在");
                return false;
            }

            // 创建或更新用户消息关联
            createOrUpdateUserMessage(messageId, userId, message.getTenantId(), message.getDomainId());

            // 更新发送状态为成功
            updateSendStatus(sendRecordId, MessageSendStatusEnum.SUCCESS.getStatus(), null);

            log.info("[processMessagePush][处理消息推送成功：messageId={}, userId={}]", messageId, userId);
            return true;
        } catch (Exception e) {
            log.error("[processMessagePush][处理消息推送异常：messageId={}, userId={}]", messageId, userId, e);
            updateSendStatus(sendRecordId, MessageSendStatusEnum.FAILED.getStatus(), e.getMessage());
            throw new MessageException(MessageException.MESSAGE_PUSH_FAILED, e.getMessage());
        }
    }

    /**
     * 创建发送记录
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 发送记录
     */
    private SystemMessageSendRecordDO createSendRecord(Integer messageId, Integer userId) {
        // 获取消息
        SystemMessageDO message = systemMessageMapper.selectById(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        SystemMessageSendRecordDO record = new SystemMessageSendRecordDO();
        record.setMessageId(messageId);
        record.setTargetType(1); // 单个用户
        record.setTargetId(String.valueOf(userId));
        record.setTenantId(message.getTenantId());
        record.setDomainId(message.getDomainId());
        record.setSendTime(LocalDateTime.now());
        record.setSendStatus(MessageSendStatusEnum.PENDING.getStatus());
        record.setRetryCount(0);

        systemMessageSendRecordMapper.insert(record);

        return record;
    }

    /**
     * 更新发送状态
     *
     * @param recordId 记录ID
     * @param status 状态
     * @param errorMessage 错误信息
     */
    private void updateSendStatus(Integer recordId, Integer status, String errorMessage) {
        SystemMessageSendRecordDO record = new SystemMessageSendRecordDO();
        record.setId(recordId);
        record.setSendStatus(status);
        record.setErrorMessage(errorMessage);

        if (status.equals(MessageSendStatusEnum.FAILED.getStatus())) {
            // 更新重试次数
            SystemMessageSendRecordDO existRecord = systemMessageSendRecordMapper.selectById(recordId);
            if (existRecord != null) {
                record.setRetryCount(existRecord.getRetryCount() + 1);
            }
        }

        systemMessageSendRecordMapper.updateById(record);
    }

    /**
     * 创建或更新用户消息关联
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     */
    private void createOrUpdateUserMessage(Integer messageId, Integer userId, Integer tenantId, Integer domainId) {
        // 查询用户消息关联
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SystemUserMessageDO> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUserMessageDO::getMessageId, messageId)
                .eq(SystemUserMessageDO::getUserId, userId);

        SystemUserMessageDO userMessage = systemUserMessageMapper.selectOne(queryWrapper);

        if (userMessage == null) {
            // 创建用户消息关联
            userMessage = new SystemUserMessageDO();
            userMessage.setMessageId(messageId);
            userMessage.setUserId(userId);
            userMessage.setTenantId(tenantId);
            userMessage.setDomainId(domainId);
            userMessage.setIsRead(false);
            userMessage.setIsDeletedByUser(false);

            systemUserMessageMapper.insert(userMessage);
        } else if (Boolean.TRUE.equals(userMessage.getIsDeletedByUser())) {
            // 如果用户已删除，则恢复
            userMessage.setIsDeletedByUser(false);
            systemUserMessageMapper.updateById(userMessage);
        }
    }
}

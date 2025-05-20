package org.example.musk.functions.message.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.functions.message.dao.SystemMessageMapper;
import org.example.musk.functions.message.dao.SystemMessageSendRecordMapper;
import org.example.musk.functions.message.enums.MessageSendStatusEnum;
import org.example.musk.functions.message.enums.MessageStatusEnum;
import org.example.musk.functions.message.enums.MessageTargetTypeEnum;
import org.example.musk.functions.message.exception.MessageException;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.entity.SystemMessageSendRecordDO;
import org.example.musk.functions.message.model.entity.SystemMessageTemplateDO;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageSendReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateSendReqVO;
import org.example.musk.functions.message.service.MessageManageService;
import org.example.musk.functions.message.service.MessagePushService;
import org.example.musk.functions.message.service.MessageSendService;
import org.example.musk.functions.message.service.MessageTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息发送服务实现类
 *
 * @author musk-functions-message
 */
@Service
@Validated
@Slf4j
@DS(MessageConstant.DB_NAME)
public class MessageSendServiceImpl implements MessageSendService {

    @Resource
    private SystemMessageMapper systemMessageMapper;

    @Resource
    private SystemMessageSendRecordMapper systemMessageSendRecordMapper;

    @Resource
    private MessageManageService messageManageService;

    @Resource
    private MessageTemplateService messageTemplateService;

    @Resource
    private MessagePushService messagePushService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendMessage(MessageSendReqVO sendReqVO) {
        // 校验消息存在
        SystemMessageDO message = messageManageService.getMessage(sendReqVO.getMessageId());
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        // 校验消息状态，只有已发布状态的消息才能发送
        if (!MessageStatusEnum.PUBLISHED.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有已发布状态的消息才能发送");
        }

        // 根据目标类型发送消息
        switch (MessageTargetTypeEnum.getByType(sendReqVO.getTargetType())) {
            case USER:
                // 发送给单个用户
                if (sendReqVO.getTargetIds() == null || sendReqVO.getTargetIds().isEmpty()) {
                    throw new MessageException(MessageException.MESSAGE_SEND_FAILED, "目标用户ID不能为空");
                }
                return sendToUsers(sendReqVO.getMessageId(), sendReqVO.getTargetIds()) > 0;
            case USER_GROUP:
                // 发送给用户组
                if (sendReqVO.getTargetIds() == null || sendReqVO.getTargetIds().isEmpty()) {
                    throw new MessageException(MessageException.MESSAGE_SEND_FAILED, "目标用户组ID不能为空");
                }
                // TODO: 实现发送给用户组的逻辑
                throw new MessageException(MessageException.MESSAGE_SEND_FAILED, "暂不支持发送给用户组");
            case ALL_USERS:
                // 发送给所有用户
                return sendToAllUsers(sendReqVO.getMessageId(), message.getTenantId(), message.getDomainId());
            default:
                throw new MessageException(MessageException.MESSAGE_SEND_FAILED, "未知的目标类型");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendToUser(Integer messageId, Integer userId) {
        // 校验消息存在
        SystemMessageDO message = messageManageService.getMessage(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        // 校验消息状态，只有已发布状态的消息才能发送
        if (!MessageStatusEnum.PUBLISHED.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有已发布状态的消息才能发送");
        }

        try {
            // 创建发送记录
            SystemMessageSendRecordDO record = createSendRecord(messageId, MessageTargetTypeEnum.USER.getType(),
                    String.valueOf(userId), message.getTenantId(), message.getDomainId());

            // 推送消息
            boolean success = messagePushService.pushMessage(messageId, userId);

            if (!success) {
                // 更新发送状态为失败
                updateSendStatus(record.getId(), MessageSendStatusEnum.FAILED.getStatus(), "推送消息失败");
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("[sendToUser][发送消息异常：messageId={}, userId={}]", messageId, userId, e);
            throw new MessageException(MessageException.MESSAGE_SEND_FAILED, e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sendToUsers(Integer messageId, List<Integer> userIds) {
        // 校验消息存在
        SystemMessageDO message = messageManageService.getMessage(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        // 校验消息状态，只有已发布状态的消息才能发送
        if (!MessageStatusEnum.PUBLISHED.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有已发布状态的消息才能发送");
        }

        // 创建发送记录
        SystemMessageSendRecordDO record = createSendRecord(messageId, MessageTargetTypeEnum.USER.getType(),
                userIds.stream().map(String::valueOf).collect(Collectors.joining(",")),
                message.getTenantId(), message.getDomainId());

        // 发送计数
        int successCount = 0;

        // 批量发送
        for (Integer userId : userIds) {
            try {
                boolean success = messagePushService.pushMessage(messageId, userId);
                if (success) {
                    successCount++;
                }
            } catch (Exception e) {
                log.error("[sendToUsers][发送消息异常：messageId={}, userId={}]", messageId, userId, e);
            }
        }

        // 更新发送状态
        if (successCount == 0) {
            updateSendStatus(record.getId(), MessageSendStatusEnum.FAILED.getStatus(), "所有消息发送失败");
        } else if (successCount < userIds.size()) {
            updateSendStatus(record.getId(), MessageSendStatusEnum.SUCCESS.getStatus(), "部分消息发送成功：" + successCount + "/" + userIds.size());
        } else {
            updateSendStatus(record.getId(), MessageSendStatusEnum.SUCCESS.getStatus(), null);
        }

        return successCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendToAllUsers(Integer messageId, Integer tenantId, Integer domainId) {
        // 校验消息存在
        SystemMessageDO message = messageManageService.getMessage(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        // 校验消息状态，只有已发布状态的消息才能发送
        if (!MessageStatusEnum.PUBLISHED.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有已发布状态的消息才能发送");
        }

        // 创建发送记录
        SystemMessageSendRecordDO record = createSendRecord(messageId, MessageTargetTypeEnum.ALL_USERS.getType(),
                null, tenantId, domainId);

        // TODO: 实现发送给所有用户的逻辑
        // 这里需要根据实际情况实现，可能需要分批次发送，或者使用异步任务

        // 更新发送状态为发送中
        updateSendStatus(record.getId(), MessageSendStatusEnum.SENDING.getStatus(), null);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer sendTemplateMessage(MessageTemplateSendReqVO templateSendReqVO) {
        // 获取模板
        SystemMessageTemplateDO template = messageTemplateService.getMessageTemplateByCode(
                templateSendReqVO.getTemplateCode(), ThreadLocalTenantContext.getTenantId(), ThreadLocalTenantContext.getDomainId());

        if (template == null) {
            throw new MessageException(MessageException.MESSAGE_TEMPLATE_NOT_EXISTS);
        }

        // 渲染模板
        Map<String, String> renderedContent = messageTemplateService.renderTemplate(template, templateSendReqVO.getTemplateParams());

        // 创建消息
        MessageCreateReqVO createReqVO = new MessageCreateReqVO();
        createReqVO.setMessageType(template.getTemplateType());
        createReqVO.setTitle(renderedContent.get("title"));
        createReqVO.setContent(renderedContent.get("content"));
        createReqVO.setImageUrl(template.getImageUrl());
        createReqVO.setPriority(template.getPriority());
        createReqVO.setIsForced(template.getIsForced());
        createReqVO.setActionType(template.getActionType());
        createReqVO.setActionUrl(template.getActionUrl());
        createReqVO.setActionParams(template.getActionParams());
        createReqVO.setPlatformType(template.getPlatformType());
        createReqVO.setTemplateId(template.getId());
        createReqVO.setDomainId(template.getDomainId());

        // 保存消息
        Integer messageId = messageManageService.createMessage(createReqVO);

        // 发布消息
        messageManageService.publishMessage(messageId);

        // 发送消息
        MessageSendReqVO sendReqVO = new MessageSendReqVO();
        sendReqVO.setMessageId(messageId);
        sendReqVO.setTargetType(templateSendReqVO.getTargetType());
        sendReqVO.setTargetIds(templateSendReqVO.getTargetIds());

        sendMessage(sendReqVO);

        return messageId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer sendTemplateMessageToUser(String templateCode, Map<String, Object> params, Integer userId) {
        // 创建模板发送请求
        MessageTemplateSendReqVO templateSendReqVO = new MessageTemplateSendReqVO();
        templateSendReqVO.setTemplateCode(templateCode);
        templateSendReqVO.setTemplateParams(params);
        templateSendReqVO.setTargetType(MessageTargetTypeEnum.USER.getType());

        List<Integer> targetIds = new ArrayList<>();
        targetIds.add(userId);
        templateSendReqVO.setTargetIds(targetIds);

        return sendTemplateMessage(templateSendReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processPendingMessages() {
        // 查询待发送的消息记录
        LambdaQueryWrapper<SystemMessageSendRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMessageSendRecordDO::getSendStatus, MessageSendStatusEnum.PENDING.getStatus())
                .le(SystemMessageSendRecordDO::getSendTime, LocalDateTime.now());

        List<SystemMessageSendRecordDO> pendingRecords = systemMessageSendRecordMapper.selectList(queryWrapper);

        if (pendingRecords.isEmpty()) {
            return;
        }

        log.info("[processPendingMessages][处理待发送消息：count={}]", pendingRecords.size());

        // 处理待发送的消息
        for (SystemMessageSendRecordDO record : pendingRecords) {
            try {
                // 更新发送状态为发送中
                updateSendStatus(record.getId(), MessageSendStatusEnum.SENDING.getStatus(), null);

                // 根据目标类型发送消息
                switch (MessageTargetTypeEnum.getByType(record.getTargetType())) {
                    case USER:
                        // 发送给单个用户
                        if (record.getTargetId() != null) {
                            Integer userId = Integer.parseInt(record.getTargetId());
                            messagePushService.pushMessage(record.getMessageId(), userId);
                        }
                        break;
                    case USER_GROUP:
                        // 发送给用户组
                        // TODO: 实现发送给用户组的逻辑
                        break;
                    case ALL_USERS:
                        // 发送给所有用户
                        // TODO: 实现发送给所有用户的逻辑
                        break;
                    default:
                        updateSendStatus(record.getId(), MessageSendStatusEnum.FAILED.getStatus(), "未知的目标类型");
                }
            } catch (Exception e) {
                log.error("[processPendingMessages][处理待发送消息异常：recordId={}]", record.getId(), e);
                updateSendStatus(record.getId(), MessageSendStatusEnum.FAILED.getStatus(), e.getMessage());
            }
        }
    }

    /**
     * 创建发送记录
     *
     * @param messageId 消息ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 发送记录
     */
    private SystemMessageSendRecordDO createSendRecord(Integer messageId, Integer targetType, String targetId,
                                                     Integer tenantId, Integer domainId) {
        SystemMessageSendRecordDO record = new SystemMessageSendRecordDO();
        record.setMessageId(messageId);
        record.setTargetType(targetType);
        record.setTargetId(targetId);
        record.setTenantId(tenantId);
        record.setDomainId(domainId);
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

        systemMessageSendRecordMapper.updateById(record);
    }
}

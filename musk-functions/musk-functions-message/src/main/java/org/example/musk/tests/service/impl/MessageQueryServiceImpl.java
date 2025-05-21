package org.example.musk.tests.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.functions.message.dao.SystemMessageMapper;
import org.example.musk.functions.message.dao.SystemUserMessageMapper;
import org.example.musk.functions.message.enums.MessageActionTypeEnum;
import org.example.musk.functions.message.enums.MessagePriorityEnum;
import org.example.musk.functions.message.enums.MessageTypeEnum;
import org.example.musk.functions.message.exception.MessageException;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.entity.SystemUserMessageDO;
import org.example.musk.functions.message.model.vo.MessageDetailVO;
import org.example.musk.functions.message.model.vo.UserMessageVO;
import org.example.musk.tests.service.MessageQueryService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * 消息查询服务实现类
 *
 * @author musk-functions-message
 */
@Service
@Validated
@Slf4j
@DS(MessageConstant.DB_NAME)
public class MessageQueryServiceImpl implements MessageQueryService {

    @Resource
    private SystemMessageMapper systemMessageMapper;

    @Resource
    private SystemUserMessageMapper systemUserMessageMapper;

    @Override
    @Cacheable(namespace = "MESSAGE", key = "'unread:' + #userId + ':' + #tenantId + ':' + #domainId", expireSeconds = MessageConstant.MESSAGE_STATS_CACHE_EXPIRE_SECONDS)
    public int getUnreadMessageCount(Integer userId, Integer tenantId, Integer domainId) {
        return systemUserMessageMapper.getUnreadMessageCount(userId, tenantId, domainId);
    }

    @Override
    public PageResult<UserMessageVO> getUserMessages(Integer userId, Integer tenantId, Integer domainId,
                                                     Integer pageNum, Integer pageSize, Boolean isRead) {
        // 创建分页对象
        Page<UserMessageVO> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        IPage<UserMessageVO> iPage = systemUserMessageMapper.getUserMessages(page, userId, tenantId, domainId, isRead);

        // 设置枚举描述
        iPage.getRecords().forEach(this::setEnumDesc);

        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public MessageDetailVO getMessageDetail(Integer messageId, Integer userId) {
        // 获取消息
        SystemMessageDO message = systemMessageMapper.selectById(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }

        // 获取用户消息关联
        SystemUserMessageDO userMessage = getUserMessage(messageId, userId);

        // 构建消息详情
        MessageDetailVO detailVO = new MessageDetailVO();
        detailVO.setMessageId(message.getId());
        detailVO.setMessageType(message.getMessageType());
        detailVO.setTitle(message.getTitle());
        detailVO.setContent(message.getContent());
        detailVO.setImageUrl(message.getImageUrl());
        detailVO.setPriority(message.getPriority());
        detailVO.setIsForced(message.getIsForced());
        detailVO.setActionType(message.getActionType());
        detailVO.setActionUrl(message.getActionUrl());
        detailVO.setActionParams(message.getActionParams());
        detailVO.setCreateTime(message.getCreateTime());

        // 设置用户相关信息
        if (userMessage != null) {
            detailVO.setIsRead(userMessage.getIsRead());
            detailVO.setReadTime(userMessage.getReadTime());
        } else {
            detailVO.setIsRead(false);
        }

        // 设置枚举描述
        setEnumDesc(detailVO);

        return detailVO;
    }

    /**
     * 获取用户消息关联
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 用户消息关联
     */
    private SystemUserMessageDO getUserMessage(Integer messageId, Integer userId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SystemUserMessageDO> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUserMessageDO::getMessageId, messageId)
                .eq(SystemUserMessageDO::getUserId, userId)
                .eq(SystemUserMessageDO::getIsDeletedByUser, false);

        return systemUserMessageMapper.selectOne(queryWrapper);
    }

    /**
     * 设置枚举描述
     *
     * @param vo 用户消息视图对象
     */
    private void setEnumDesc(UserMessageVO vo) {
        // 设置消息类型描述
        if (vo.getMessageType() != null) {
            try {
                vo.setMessageTypeDesc(Objects.requireNonNull(MessageTypeEnum.getByType(vo.getMessageType())).getDesc());
            } catch (Exception e) {
                vo.setMessageTypeDesc("未知");
            }
        }

        // 设置优先级描述
        if (vo.getPriority() != null) {
            try {
                vo.setPriorityDesc(Objects.requireNonNull(MessagePriorityEnum.getByPriority(vo.getPriority())).getDesc());
            } catch (Exception e) {
                vo.setPriorityDesc("未知");
            }
        }

        // 设置操作类型描述
        if (vo.getActionType() != null) {
            try {
                vo.setActionTypeDesc(Objects.requireNonNull(MessageActionTypeEnum.getByType(vo.getActionType())).getDesc());
            } catch (Exception e) {
                vo.setActionTypeDesc("未知");
            }
        }
    }

    /**
     * 设置枚举描述
     *
     * @param vo 消息详情视图对象
     */
    private void setEnumDesc(MessageDetailVO vo) {
        // 设置消息类型描述
        if (vo.getMessageType() != null) {
            try {
                vo.setMessageTypeDesc(Objects.requireNonNull(MessageTypeEnum.getByType(vo.getMessageType())).getDesc());
            } catch (Exception e) {
                vo.setMessageTypeDesc("未知");
            }
        }

        // 设置优先级描述
        if (vo.getPriority() != null) {
            try {
                vo.setPriorityDesc(Objects.requireNonNull(MessagePriorityEnum.getByPriority(vo.getPriority())).getDesc());
            } catch (Exception e) {
                vo.setPriorityDesc("未知");
            }
        }

        // 设置操作类型描述
        if (vo.getActionType() != null) {
            try {
                vo.setActionTypeDesc(Objects.requireNonNull(MessageActionTypeEnum.getByType(vo.getActionType())).getDesc());
            } catch (Exception e) {
                vo.setActionTypeDesc("未知");
            }
        }
    }
}

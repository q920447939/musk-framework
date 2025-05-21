package org.example.musk.tests.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.message.constant.MessageConstant;
import org.example.musk.functions.message.dao.SystemMessageMapper;
import org.example.musk.functions.message.enums.MessageStatusEnum;
import org.example.musk.functions.message.exception.MessageException;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageRespVO;
import org.example.musk.functions.message.model.vo.MessageUpdateReqVO;
import org.example.musk.tests.service.MessageManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 消息管理服务实现类
 *
 * @author musk-functions-message
 */
@Service
@Validated
@Slf4j
@DS(MessageConstant.DB_NAME)
public class MessageManageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessageDO> implements MessageManageService {

    @Resource
    private SystemMessageMapper systemMessageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createMessage(MessageCreateReqVO createReqVO) {
        // 转换请求VO为DO
        SystemMessageDO message = BeanUtils.toBean(createReqVO, SystemMessageDO.class);

        // 设置默认值
        if (message.getPriority() == null) {
            message.setPriority(0); // 默认普通优先级
        }
        if (message.getIsForced() == null) {
            message.setIsForced(false); // 默认非强制消息
        }
        if (message.getActionType() == null) {
            message.setActionType(0); // 默认无操作
        }

        // 设置状态为草稿
        message.setStatus(MessageStatusEnum.DRAFT.getStatus());

        // 保存消息
        save(message);

        log.info("[createMessage][创建消息成功：{}]", message.getId());
        return message.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'message:' + #updateReqVO.id")
    public boolean updateMessage(MessageUpdateReqVO updateReqVO) {
        // 校验消息存在
        SystemMessageDO message = validateMessageExists(updateReqVO.getId());

        // 校验消息状态，只有草稿状态的消息才能更新
        if (!MessageStatusEnum.DRAFT.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有草稿状态的消息才能更新");
        }

        // 转换请求VO为DO
        SystemMessageDO updateObj = BeanUtils.toBean(updateReqVO, SystemMessageDO.class);

        // 更新消息
        boolean success = updateById(updateObj);

        if (success) {
            log.info("[updateMessage][更新消息成功：{}]", updateReqVO.getId());
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'message:' + #messageId")
    public boolean deleteMessage(Integer messageId) {
        // 校验消息存在
        SystemMessageDO message = validateMessageExists(messageId);

        // 校验消息状态，只有草稿状态的消息才能删除
        if (!MessageStatusEnum.DRAFT.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有草稿状态的消息才能删除");
        }

        // 删除消息
        boolean success = removeById(messageId);

        if (success) {
            log.info("[deleteMessage][删除消息成功：{}]", messageId);
        }

        return success;
    }

    @Override
    @Cacheable(namespace = "MESSAGE", key = "'message:' + #messageId", expireSeconds = MessageConstant.MESSAGE_CACHE_EXPIRE_SECONDS)
    public SystemMessageDO getMessage(Integer messageId) {
        return getById(messageId);
    }

    @Override
    public List<SystemMessageDO> getMessageList(Integer tenantId, Integer domainId, Integer platformType, Integer status) {
        LambdaQueryWrapper<SystemMessageDO> queryWrapper = new LambdaQueryWrapper<>();

        // 设置查询条件
        if (tenantId != null) {
            queryWrapper.eq(SystemMessageDO::getTenantId, tenantId);
        }
        if (domainId != null) {
            queryWrapper.eq(SystemMessageDO::getDomainId, domainId);
        }
        if (platformType != null) {
            queryWrapper.eq(SystemMessageDO::getPlatformType, platformType);
        }
        if (status != null) {
            queryWrapper.eq(SystemMessageDO::getStatus, status);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc(SystemMessageDO::getCreateTime);

        return list(queryWrapper);
    }

    @Override
    public PageResult<MessageRespVO> getMessagePage(Integer tenantId, Integer domainId, Integer platformType,
                                                    Integer status, Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<SystemMessageDO> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<SystemMessageDO> queryWrapper = new LambdaQueryWrapper<>();

        // 设置查询条件
        if (tenantId != null) {
            queryWrapper.eq(SystemMessageDO::getTenantId, tenantId);
        }
        if (domainId != null) {
            queryWrapper.eq(SystemMessageDO::getDomainId, domainId);
        }
        if (platformType != null) {
            queryWrapper.eq(SystemMessageDO::getPlatformType, platformType);
        }
        if (status != null) {
            queryWrapper.eq(SystemMessageDO::getStatus, status);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc(SystemMessageDO::getCreateTime);

        // 执行分页查询
        Page<SystemMessageDO> resultPage = page(page, queryWrapper);

        // 转换结果
        List<MessageRespVO> voList = BeanUtils.toBean(resultPage.getRecords(), MessageRespVO.class);

        // 设置枚举描述
        voList.forEach(this::setEnumDesc);

        return new PageResult<>(voList, resultPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'message:' + #messageId")
    public boolean publishMessage(Integer messageId) {
        // 校验消息存在
        SystemMessageDO message = validateMessageExists(messageId);

        // 校验消息状态，只有草稿状态的消息才能发布
        if (!MessageStatusEnum.DRAFT.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有草稿状态的消息才能发布");
        }

        // 更新消息状态为已发布
        SystemMessageDO updateObj = new SystemMessageDO();
        updateObj.setId(messageId);
        updateObj.setStatus(MessageStatusEnum.PUBLISHED.getStatus());

        // 如果没有设置生效时间，则设置为当前时间
        if (message.getStartTime() == null) {
            updateObj.setStartTime(LocalDateTime.now());
        }

        boolean success = updateById(updateObj);

        if (success) {
            log.info("[publishMessage][发布消息成功：{}]", messageId);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MESSAGE", pattern = "'message:' + #messageId")
    public boolean unpublishMessage(Integer messageId) {
        // 校验消息存在
        SystemMessageDO message = validateMessageExists(messageId);

        // 校验消息状态，只有已发布状态的消息才能取消发布
        if (!MessageStatusEnum.PUBLISHED.getStatus().equals(message.getStatus())) {
            throw new MessageException(MessageException.MESSAGE_STATUS_NOT_ALLOWED, "只有已发布状态的消息才能取消发布");
        }

        // 更新消息状态为草稿
        SystemMessageDO updateObj = new SystemMessageDO();
        updateObj.setId(messageId);
        updateObj.setStatus(MessageStatusEnum.DRAFT.getStatus());

        boolean success = updateById(updateObj);

        if (success) {
            log.info("[unpublishMessage][取消发布消息成功：{}]", messageId);
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkMessageStatus() {
        LocalDateTime now = LocalDateTime.now();

        // 查询已发布且有结束时间的消息
        LambdaQueryWrapper<SystemMessageDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMessageDO::getStatus, MessageStatusEnum.PUBLISHED.getStatus())
                .isNotNull(SystemMessageDO::getEndTime)
                .lt(SystemMessageDO::getEndTime, now);

        List<SystemMessageDO> expiredMessages = list(queryWrapper);

        if (!expiredMessages.isEmpty()) {
            // 批量更新消息状态为已过期
            for (SystemMessageDO message : expiredMessages) {
                SystemMessageDO updateObj = new SystemMessageDO();
                updateObj.setId(message.getId());
                updateObj.setStatus(MessageStatusEnum.EXPIRED.getStatus());
                updateById(updateObj);

                log.info("[checkMessageStatus][消息已过期：{}]", message.getId());
            }
        }
    }

    /**
     * 校验消息是否存在
     *
     * @param messageId 消息ID
     * @return 消息对象
     */
    private SystemMessageDO validateMessageExists(Integer messageId) {
        SystemMessageDO message = getById(messageId);
        if (message == null) {
            throw new MessageException(MessageException.MESSAGE_NOT_EXISTS);
        }
        return message;
    }

    /**
     * 设置枚举描述
     *
     * @param vo 消息响应VO
     */
    private void setEnumDesc(MessageRespVO vo) {
        // 设置消息类型描述
        if (vo.getMessageType() != null) {
            try {
                vo.setMessageTypeDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessageTypeEnum.getByType(vo.getMessageType())).getDesc());
            } catch (Exception e) {
                vo.setMessageTypeDesc("未知");
            }
        }

        // 设置优先级描述
        if (vo.getPriority() != null) {
            try {
                vo.setPriorityDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessagePriorityEnum.getByPriority(vo.getPriority())).getDesc());
            } catch (Exception e) {
                vo.setPriorityDesc("未知");
            }
        }

        // 设置操作类型描述
        if (vo.getActionType() != null) {
            try {
                vo.setActionTypeDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessageActionTypeEnum.getByType(vo.getActionType())).getDesc());
            } catch (Exception e) {
                vo.setActionTypeDesc("未知");
            }
        }

        // 设置状态描述
        if (vo.getStatus() != null) {
            try {
                vo.setStatusDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessageStatusEnum.getByStatus(vo.getStatus())).getDesc());
            } catch (Exception e) {
                vo.setStatusDesc("未知");
            }
        }

        // 设置平台类型描述
        if (vo.getPlatformType() != null) {
            try {
                vo.setPlatformTypeDesc(Objects.requireNonNull(org.example.musk.functions.message.enums.MessagePlatformTypeEnum.getByType(vo.getPlatformType())).getDesc());
            } catch (Exception e) {
                vo.setPlatformTypeDesc("未知");
            }
        }
    }
}

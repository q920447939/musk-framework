package org.example.musk.tests.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.functions.message.model.dto.SystemMessageCreateReqDTO;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageSendReqVO;
import org.example.musk.tests.service.MessageManageService;
import org.example.musk.tests.service.MessageSendService;
import org.example.musk.tests.service.SystemMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 系统消息服务实现类
 *
 * @author musk-functions-message
 */
@Service
@Validated
@Slf4j
public class SystemMessageServiceImpl implements SystemMessageService {

    @Resource
    private MessageManageService messageManageService;

    @Resource
    private MessageSendService messageSendService;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public Integer createAndSendMessage(SystemMessageCreateReqDTO messageDTO, Integer targetType, List<Integer> targetIds) {
        // 创建消息
        Integer messageId = createMessage(messageDTO);

        // 发布消息
        messageManageService.publishMessage(messageId);

        // 发送消息
        sendMessage(messageId, targetType, targetIds);

        return messageId;
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public Integer createMessage(SystemMessageCreateReqDTO messageDTO) {
        // 转换DTO为VO
        MessageCreateReqVO createReqVO = BeanUtils.toBean(messageDTO, MessageCreateReqVO.class);

        // 创建消息
        return messageManageService.createMessage(createReqVO);
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public boolean sendMessage(Integer messageId, Integer targetType, List<Integer> targetIds) {
        // 创建发送请求
        MessageSendReqVO sendReqVO = new MessageSendReqVO();
        sendReqVO.setMessageId(messageId);
        sendReqVO.setTargetType(targetType);
        sendReqVO.setTargetIds(targetIds);

        // 发送消息
        return messageSendService.sendMessage(sendReqVO);
    }
}

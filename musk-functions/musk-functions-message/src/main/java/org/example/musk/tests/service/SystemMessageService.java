package org.example.musk.tests.service;

import org.example.musk.functions.message.model.dto.SystemMessageCreateReqDTO;

import java.util.List;

/**
 * 系统消息服务接口
 *
 * 提供简化的消息创建和发送功能
 *
 * @author musk-functions-message
 */
public interface SystemMessageService {

    /**
     * 创建并发送消息
     *
     * @param messageDTO 消息创建请求
     * @param targetType 目标类型(1:单个用户 2:用户组 3:全部用户)
     * @param targetIds 目标ID列表(用户ID或用户组ID)
     * @return 消息ID
     */
    Integer createAndSendMessage(SystemMessageCreateReqDTO messageDTO, Integer targetType, List<Integer> targetIds);

    /**
     * 创建消息
     *
     * @param messageDTO 消息创建请求
     * @return 消息ID
     */
    Integer createMessage(SystemMessageCreateReqDTO messageDTO);

    /**
     * 发送消息
     *
     * @param messageId 消息ID
     * @param targetType 目标类型(1:单个用户 2:用户组 3:全部用户)
     * @param targetIds 目标ID列表(用户ID或用户组ID)
     * @return 是否发送成功
     */
    boolean sendMessage(Integer messageId, Integer targetType, List<Integer> targetIds);
}

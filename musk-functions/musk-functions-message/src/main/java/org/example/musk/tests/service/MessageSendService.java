package org.example.musk.tests.service;

import org.example.musk.functions.message.model.vo.MessageSendReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateSendReqVO;

import java.util.List;
import java.util.Map;

/**
 * 消息发送服务接口
 *
 * @author musk-functions-message
 */
public interface MessageSendService {

    /**
     * 发送消息
     *
     * @param sendReqVO 发送消息请求
     * @return 是否发送成功
     */
    boolean sendMessage(MessageSendReqVO sendReqVO);

    /**
     * 发送消息给单个用户
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否发送成功
     */
    boolean sendToUser(Integer messageId, Integer userId);

    /**
     * 发送消息给多个用户
     *
     * @param messageId 消息ID
     * @param userIds 用户ID列表
     * @return 成功发送的用户数量
     */
    int sendToUsers(Integer messageId, List<Integer> userIds);

    /**
     * 发送消息给所有用户
     *
     * @param messageId 消息ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 是否发送成功
     */
    boolean sendToAllUsers(Integer messageId, Integer tenantId, Integer domainId);

    /**
     * 使用模板发送消息
     *
     * @param templateSendReqVO 使用模板发送消息请求
     * @return 消息ID
     */
    Integer sendTemplateMessage(MessageTemplateSendReqVO templateSendReqVO);

    /**
     * 根据模板发送消息给单个用户
     *
     * @param templateCode 模板编码
     * @param params 模板参数
     * @param userId 用户ID
     * @return 消息ID
     */
    Integer sendTemplateMessageToUser(String templateCode, Map<String, Object> params, Integer userId);

    /**
     * 处理待发送的消息
     * 定时任务调用，处理状态为待发送的消息
     */
    void processPendingMessages();
}

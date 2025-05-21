package org.example.musk.tests.service;

/**
 * 消息推送服务接口
 *
 * @author musk-functions-message
 */
public interface MessagePushService {

    /**
     * 推送消息
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否推送成功
     */
    boolean pushMessage(Integer messageId, Integer userId);

    /**
     * 处理消息推送
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @param sendRecordId 发送记录ID
     * @return 是否处理成功
     */
    boolean processMessagePush(Integer messageId, Integer userId, Integer sendRecordId);
}

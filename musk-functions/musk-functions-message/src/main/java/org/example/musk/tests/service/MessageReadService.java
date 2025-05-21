package org.example.musk.tests.service;

/**
 * 消息读取服务接口
 *
 * @author musk-functions-message
 */
public interface MessageReadService {

    /**
     * 标记消息为已读
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否标记成功
     */
    boolean markAsRead(Integer messageId, Integer userId);

    /**
     * 标记所有消息为已读
     *
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 标记成功的消息数量
     */
    int markAllAsRead(Integer userId, Integer tenantId, Integer domainId);

    /**
     * 用户删除消息
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUserMessage(Integer messageId, Integer userId);
}

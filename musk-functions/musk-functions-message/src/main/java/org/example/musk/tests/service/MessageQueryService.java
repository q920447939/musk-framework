package org.example.musk.tests.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.message.model.vo.MessageDetailVO;
import org.example.musk.functions.message.model.vo.UserMessageVO;

/**
 * 消息查询服务接口
 *
 * @author musk-functions-message
 */
public interface MessageQueryService {

    /**
     * 获取用户未读消息数量
     *
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 未读消息数量
     */
    int getUnreadMessageCount(Integer userId, Integer tenantId, Integer domainId);

    /**
     * 获取用户消息列表
     *
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param isRead 是否已读（null表示全部）
     * @return 消息分页列表
     */
    PageResult<UserMessageVO> getUserMessages(Integer userId, Integer tenantId, Integer domainId,
                                              Integer pageNum, Integer pageSize, Boolean isRead);

    /**
     * 获取消息详情
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 消息详情
     */
    MessageDetailVO getMessageDetail(Integer messageId, Integer userId);
}

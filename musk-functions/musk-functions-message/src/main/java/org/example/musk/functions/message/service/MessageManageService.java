package org.example.musk.functions.message.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.message.model.entity.SystemMessageDO;
import org.example.musk.functions.message.model.vo.MessageCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageRespVO;
import org.example.musk.functions.message.model.vo.MessageUpdateReqVO;

import java.util.List;

/**
 * 消息管理服务接口
 *
 * @author musk-functions-message
 */
public interface MessageManageService {

    /**
     * 创建消息
     *
     * @param createReqVO 创建消息请求
     * @return 消息ID
     */
    Integer createMessage(MessageCreateReqVO createReqVO);

    /**
     * 更新消息
     *
     * @param updateReqVO 更新消息请求
     * @return 是否更新成功
     */
    boolean updateMessage(MessageUpdateReqVO updateReqVO);

    /**
     * 删除消息
     *
     * @param messageId 消息ID
     * @return 是否删除成功
     */
    boolean deleteMessage(Integer messageId);

    /**
     * 获取消息详情
     *
     * @param messageId 消息ID
     * @return 消息详情
     */
    SystemMessageDO getMessage(Integer messageId);

    /**
     * 获取消息列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param platformType 平台类型
     * @param status 状态
     * @return 消息列表
     */
    List<SystemMessageDO> getMessageList(Integer tenantId, Integer domainId, Integer platformType, Integer status);

    /**
     * 分页获取消息列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param platformType 平台类型
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 消息分页列表
     */
    PageResult<MessageRespVO> getMessagePage(Integer tenantId, Integer domainId, Integer platformType,
                                             Integer status, Integer pageNum, Integer pageSize);

    /**
     * 发布消息
     *
     * @param messageId 消息ID
     * @return 是否发布成功
     */
    boolean publishMessage(Integer messageId);

    /**
     * 取消发布消息
     *
     * @param messageId 消息ID
     * @return 是否取消成功
     */
    boolean unpublishMessage(Integer messageId);

    /**
     * 检查消息状态
     * 检查已发布消息是否过期，并更新状态
     */
    void checkMessageStatus();
}

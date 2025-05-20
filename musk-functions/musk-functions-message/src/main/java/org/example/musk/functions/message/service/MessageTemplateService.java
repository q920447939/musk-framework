package org.example.musk.functions.message.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.message.model.entity.SystemMessageTemplateDO;
import org.example.musk.functions.message.model.vo.MessageTemplateCreateReqVO;
import org.example.musk.functions.message.model.vo.MessageTemplateRespVO;

import java.util.List;
import java.util.Map;

/**
 * 消息模板服务接口
 *
 * @author musk-functions-message
 */
public interface MessageTemplateService {

    /**
     * 创建消息模板
     *
     * @param createReqVO 创建消息模板请求
     * @return 模板ID
     */
    Integer createMessageTemplate(MessageTemplateCreateReqVO createReqVO);

    /**
     * 更新消息模板
     *
     * @param template 模板对象
     * @return 是否更新成功
     */
    boolean updateMessageTemplate(SystemMessageTemplateDO template);

    /**
     * 删除消息模板
     *
     * @param templateId 模板ID
     * @return 是否删除成功
     */
    boolean deleteMessageTemplate(Integer templateId);

    /**
     * 获取消息模板详情
     *
     * @param templateId 模板ID
     * @return 模板详情
     */
    SystemMessageTemplateDO getMessageTemplate(Integer templateId);

    /**
     * 根据模板编码获取模板
     *
     * @param templateCode 模板编码
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 消息模板
     */
    SystemMessageTemplateDO getMessageTemplateByCode(String templateCode, Integer tenantId, Integer domainId);

    /**
     * 获取消息模板列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<SystemMessageTemplateDO> getMessageTemplateList(Integer tenantId, Integer domainId, Integer templateType);

    /**
     * 分页获取消息模板列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param templateType 模板类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 模板分页列表
     */
    PageResult<MessageTemplateRespVO> getMessageTemplatePage(Integer tenantId, Integer domainId, Integer templateType,
                                                             Integer pageNum, Integer pageSize);

    /**
     * 渲染消息模板
     *
     * @param template 模板对象
     * @param params 模板参数
     * @return 渲染后的消息内容
     */
    Map<String, String> renderTemplate(SystemMessageTemplateDO template, Map<String, Object> params);
}

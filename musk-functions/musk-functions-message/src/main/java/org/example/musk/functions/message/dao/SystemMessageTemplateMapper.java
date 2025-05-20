package org.example.musk.functions.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.musk.functions.message.model.entity.SystemMessageTemplateDO;

/**
 * 消息模板 Mapper
 *
 * @author musk-functions-message
 */
@Mapper
public interface SystemMessageTemplateMapper extends BaseMapper<SystemMessageTemplateDO> {

    /**
     * 根据模板编码获取模板
     *
     * @param templateCode 模板编码
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 消息模板
     */
    SystemMessageTemplateDO getByTemplateCode(@Param("templateCode") String templateCode,
                                              @Param("tenantId") Integer tenantId,
                                              @Param("domainId") Integer domainId);
}

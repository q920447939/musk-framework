package org.example.musk.functions.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.musk.functions.message.model.entity.SystemUserMessageDO;
import org.example.musk.functions.message.model.vo.UserMessageVO;

/**
 * 用户消息关联 Mapper
 *
 * @author musk-functions-message
 */
@Mapper
public interface SystemUserMessageMapper extends BaseMapper<SystemUserMessageDO> {

    /**
     * 获取用户消息列表
     *
     * @param page 分页参数
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param isRead 是否已读
     * @return 用户消息列表
     */
    IPage<UserMessageVO> getUserMessages(Page<UserMessageVO> page,
                                         @Param("userId") Integer userId,
                                         @Param("tenantId") Integer tenantId,
                                         @Param("domainId") Integer domainId,
                                         @Param("isRead") Boolean isRead);

    /**
     * 获取用户未读消息数量
     *
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 未读消息数量
     */
    int getUnreadMessageCount(@Param("userId") Integer userId,
                              @Param("tenantId") Integer tenantId,
                              @Param("domainId") Integer domainId);
}

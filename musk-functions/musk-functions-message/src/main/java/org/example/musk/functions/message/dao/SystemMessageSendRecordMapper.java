package org.example.musk.functions.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.message.model.entity.SystemMessageSendRecordDO;

/**
 * 消息发送记录 Mapper
 *
 * @author musk-functions-message
 */
@Mapper
public interface SystemMessageSendRecordMapper extends BaseMapper<SystemMessageSendRecordDO> {
}

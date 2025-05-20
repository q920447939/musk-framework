package org.example.musk.functions.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.message.model.entity.SystemMessageDO;

/**
 * 消息 Mapper
 *
 * @author musk-functions-message
 */
@Mapper
public interface SystemMessageMapper extends BaseMapper<SystemMessageDO> {
}

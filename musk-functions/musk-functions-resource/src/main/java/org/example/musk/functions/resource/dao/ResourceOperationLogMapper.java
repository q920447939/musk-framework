package org.example.musk.functions.resource.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.musk.functions.resource.entity.SystemResourceOperationLogDO;

import java.util.List;

/**
 * 资源操作日志 Mapper
 *
 * @author musk-functions-resource
 */
@Mapper
public interface ResourceOperationLogMapper extends BaseMapper<SystemResourceOperationLogDO> {

}

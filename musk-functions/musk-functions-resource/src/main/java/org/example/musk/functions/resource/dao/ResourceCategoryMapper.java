package org.example.musk.functions.resource.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.resource.entity.SystemResourceCategoryDO;

/**
 * 资源分类 Mapper
 *
 * @author musk-functions-resource
 */
@Mapper
public interface ResourceCategoryMapper extends BaseMapper<SystemResourceCategoryDO> {

}

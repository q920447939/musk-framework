package org.example.musk.functions.system.menu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.system.menu.entity.SystemMenuDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

/**
 * 菜单 Mapper
 *
 */
@Mapper
public interface SystemMenuMapper extends BaseMapperX<SystemMenuDO> {
}

package org.example.musk.functions.menu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.menu.entity.MenuDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

/**
 * 菜单 Mapper
 *
 * @author musk-functions-menu
 */
@Mapper
public interface MenuMapper extends BaseMapperX<MenuDO> {
}

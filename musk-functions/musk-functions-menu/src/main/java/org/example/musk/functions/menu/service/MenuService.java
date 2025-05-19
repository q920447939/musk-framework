package org.example.musk.functions.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.functions.menu.controller.vo.MenuTreeVO;
import org.example.musk.functions.menu.entity.MenuDO;

import java.util.List;

/**
 * 菜单 Service 接口
 *
 * @author musk-functions-menu
 */
public interface MenuService extends IService<MenuDO> {

    /**
     * 创建菜单
     *
     * @param menu 菜单信息
     * @return 菜单ID
     */
    Long createMenu(MenuDO menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     */
    void updateMenu(MenuDO menu);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    void deleteMenu(Long id);

    /**
     * 获取菜单
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    MenuDO getMenu(Long id);

    /**
     * 获取指定域下的所有菜单
     *
     * @param domain 域
     * @return 菜单列表
     */
    List<MenuDO> getMenusByDomain(Integer domain);

    /**
     * 获取指定父菜单下的子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<MenuDO> getChildMenus(Long parentId);

    /**
     * 获取菜单树
     *
     * @param domain 域
     * @return 菜单树
     */
    List<MenuTreeVO> getMenuTree(Integer domain);

    /**
     * 获取指定租户下的菜单树
     *
     * @param tenantId 租户ID
     * @param domain 域
     * @return 菜单树
     */
    List<MenuTreeVO> getMenuTreeByTenant(Integer tenantId, Integer domain);
}

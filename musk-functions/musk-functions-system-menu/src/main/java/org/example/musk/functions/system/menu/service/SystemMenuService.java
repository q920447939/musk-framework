package org.example.musk.functions.system.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.functions.system.menu.controller.vo.SystemMenuTreeVO;
import org.example.musk.functions.system.menu.entity.SystemMenuDO;

import java.util.List;

/**
 * 菜单 Service 接口
 *
 * @author musk-functions-menu
 */
public interface SystemMenuService extends IService<SystemMenuDO> {

    /**
     * 创建菜单
     *
     * @param menu 菜单信息
     * @return 菜单ID
     */
    Long createMenu(SystemMenuDO menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单信息
     */
    void updateMenu(SystemMenuDO menu);

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
    SystemMenuDO getMenu(Long id);

    /**
     * 获取指定域下的所有菜单
     *
     * @param domain 域
     * @return 菜单列表
     */
    List<SystemMenuDO> getMenusByDomain(Integer domain);

    /**
     * 获取指定父菜单下的子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<SystemMenuDO> getChildMenus(Long parentId);

    /**
     * 获取菜单树
     *
     * @param domain 域
     * @return 菜单树
     */
    List<SystemMenuTreeVO> getMenuTree(Integer domain);

    /**
     * 获取指定租户下的菜单树
     *
     * @param tenantId 租户ID
     * @param domain 域
     * @return 菜单树
     */
    List<SystemMenuTreeVO> getMenuTreeByTenant(Integer tenantId, Integer domain);
}

package org.example.musk.functions.menu.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.functions.menu.constant.MenuConstant;
import org.example.musk.functions.menu.controller.vo.MenuTreeVO;
import org.example.musk.functions.menu.entity.MenuDO;
import org.example.musk.functions.menu.exception.MenuException;
import org.example.musk.functions.menu.mapper.MenuMapper;
import org.example.musk.functions.menu.service.MenuService;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单 Service 实现类
 *
 * @author musk-functions-menu
 */
@Service
@Validated
@Slf4j
@DS(MenuConstant.DB_NAME)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDO> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Long createMenu(MenuDO menu) {
        // 校验菜单层级和父菜单ID的一致性
        validateMenuLevel(menu);
        // 保存菜单
        save(menu);
        // 清除缓存
        clearMenuCache(menu.getTenantId(), menu.getDomain());
        return menu.getId();
    }

    @Override
    public void updateMenu(MenuDO menu) {
        // 校验菜单存在
        validateMenuExists(menu.getId());
        // 校验菜单层级和父菜单ID的一致性
        validateMenuLevel(menu);
        // 更新菜单
        updateById(menu);
        // 清除缓存
        clearMenuCache(menu.getTenantId(), menu.getDomain());
    }

    @Override
    public void deleteMenu(Long id) {
        // 校验菜单存在
        MenuDO menu = validateMenuExists(id);
        // 检查是否有子菜单
        if (hasChildMenu(id)) {
            throw new MenuException(MenuException.MENU_HAS_CHILDREN);
        }
        // 删除菜单
        removeById(id);
        // 清除缓存
        clearMenuCache(menu.getTenantId(), menu.getDomain());
    }

    @Override
    public MenuDO getMenu(Long id) {
        return getById(id);
    }

    @Override
    public List<MenuDO> getMenusByDomain(Integer domain) {
        return list(new LambdaQueryWrapperX<MenuDO>()
                .eq(MenuDO::getDomain, domain));
    }

    @Override
    public List<MenuDO> getChildMenus(Long parentId) {
        return list(new LambdaQueryWrapperX<MenuDO>()
                .eq(MenuDO::getParentMenuId, parentId));
    }

    @Override
    public List<MenuTreeVO> getMenuTree(Integer domain) {
        // 获取所有菜单
        List<MenuDO> menus = getMenusByDomain(domain);
        // 构建菜单树
        return buildMenuTree(menus, null);
    }

    @Override
    public List<MenuTreeVO> getMenuTreeByTenant(Integer tenantId, Integer domain) {
        // 尝试从缓存获取
        String cacheKey = MenuConstant.MENU_TREE_CACHE_KEY + tenantId + ":" + domain;
        @SuppressWarnings("unchecked")
        List<MenuTreeVO> menuTree = (List<MenuTreeVO>) redisUtil.get(cacheKey);
        if (menuTree != null) {
            return menuTree;
        }

        // 缓存未命中，从数据库获取
        List<MenuDO> menus = list(new LambdaQueryWrapperX<MenuDO>()
                .eq(MenuDO::getTenantId, tenantId)
                .eq(MenuDO::getDomain, domain));

        // 构建菜单树
        menuTree = buildMenuTree(menus, null);

        // 存入缓存
        redisUtil.set(cacheKey, menuTree, MenuConstant.MENU_CACHE_EXPIRE_SECONDS);

        return menuTree;
    }

    /**
     * 校验菜单层级和父菜单ID的一致性
     *
     * @param menu 菜单信息
     */
    private void validateMenuLevel(MenuDO menu) {
        if (MenuConstant.ROOT_MENU_LEVEL.equals(menu.getMenuLevel())) {
            if (menu.getParentMenuId() != null) {
                throw new MenuException(MenuException.MENU_LEVEL_ERROR);
            }
        } else {
            if (menu.getParentMenuId() == null) {
                throw new MenuException(MenuException.MENU_LEVEL_ERROR);
            }
            // 校验父菜单存在
            MenuDO parentMenu = getById(menu.getParentMenuId());
            if (parentMenu == null) {
                throw new MenuException(MenuException.PARENT_MENU_NOT_EXISTS);
            }
            // 校验父菜单层级
            if (parentMenu.getMenuLevel() != menu.getMenuLevel() - 1) {
                throw new MenuException(MenuException.MENU_LEVEL_ERROR);
            }
        }
    }

    /**
     * 校验菜单存在
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    private MenuDO validateMenuExists(Long id) {
        MenuDO menu = getById(id);
        if (menu == null) {
            throw new MenuException(MenuException.MENU_NOT_EXISTS);
        }
        return menu;
    }

    /**
     * 检查是否有子菜单
     *
     * @param id 菜单ID
     * @return 是否有子菜单
     */
    private boolean hasChildMenu(Long id) {
        return count(new LambdaQueryWrapperX<MenuDO>()
                .eq(MenuDO::getParentMenuId, id)) > 0;
    }

    /**
     * 构建菜单树
     *
     * @param menus 菜单列表
     * @param parentId 父菜单ID
     * @return 菜单树
     */
    private List<MenuTreeVO> buildMenuTree(List<MenuDO> menus, Long parentId) {
        if (menus == null || menus.isEmpty()) {
            return new ArrayList<>();
        }

        return menus.stream()
                .filter(menu -> (parentId == null && menu.getParentMenuId() == null) ||
                        (parentId != null && parentId.equals(menu.getParentMenuId())))
                .map(menu -> {
                    MenuTreeVO treeNode = BeanUtils.toBean(menu, MenuTreeVO.class);
                    treeNode.setChildren(buildMenuTree(menus, menu.getId()));
                    return treeNode;
                })
                .collect(Collectors.toList());
    }

    /**
     * 清除菜单缓存
     *
     * @param tenantId 租户ID
     * @param domain 域
     */
    private void clearMenuCache(Integer tenantId, Integer domain) {
        String cacheKey = MenuConstant.MENU_TREE_CACHE_KEY + tenantId + ":" + domain;
        redisUtil.del(cacheKey);
    }
}

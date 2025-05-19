package org.example.musk.functions.menu.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.pojo.CommonResult;
import org.example.musk.common.util.commonResult.CommonResultUtils;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.functions.menu.controller.vo.MenuCreateReqVO;
import org.example.musk.functions.menu.controller.vo.MenuRespVO;
import org.example.musk.functions.menu.controller.vo.MenuTreeVO;
import org.example.musk.functions.menu.controller.vo.MenuUpdateReqVO;
import org.example.musk.functions.menu.entity.MenuDO;
import org.example.musk.functions.menu.service.MenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单 Controller
 *
 * @author musk-functions-menu
 */
@RestController
@RequestMapping("/api/menu")
@Validated
@Slf4j
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 创建菜单
     *
     * @param createReqVO 创建菜单请求
     * @return 菜单ID
     */
    @PostMapping("/create")
    public CommonResult<Long> createMenu(@Valid @RequestBody MenuCreateReqVO createReqVO) {
        MenuDO menu = BeanUtils.toBean(createReqVO, MenuDO.class);
        // 设置租户ID
        menu.setTenantId(ThreadLocalTenantContext.getTenantId());
        return CommonResult.success(menuService.createMenu(menu));
    }

    /**
     * 更新菜单
     *
     * @param updateReqVO 更新菜单请求
     * @return 是否成功
     */
    @PutMapping("/update")
    public CommonResult<Boolean> updateMenu(@Valid @RequestBody MenuUpdateReqVO updateReqVO) {
        MenuDO menu = BeanUtils.toBean(updateReqVO, MenuDO.class);
        // 设置租户ID
        menu.setTenantId(ThreadLocalTenantContext.getTenantId());
        menuService.updateMenu(menu);
        return CommonResult.success(true);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 是否成功
     */
    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteMenu(@RequestParam("id") Long id) {
        menuService.deleteMenu(id);
        return CommonResult.success(true);
    }

    /**
     * 获取菜单
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    @GetMapping("/get")
    public CommonResult<MenuRespVO> getMenu(@RequestParam("id") Long id) {
        MenuDO menu = menuService.getMenu(id);
        return CommonResultUtils.wrapEmptyObjResult(menu, () -> BeanUtils.toBean(menu, MenuRespVO.class));
    }

    /**
     * 获取指定域下的所有菜单
     *
     * @param domain 域
     * @return 菜单列表
     */
    @GetMapping("/list-by-domain")
    public CommonResult<List<MenuRespVO>> getMenusByDomain(@RequestParam("domain") Integer domain) {
        List<MenuDO> menus = menuService.getMenusByDomain(domain);
        return CommonResult.success(BeanUtils.toBean(menus, MenuRespVO.class));
    }

    /**
     * 获取指定父菜单下的子菜单
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    @GetMapping("/list-by-parent")
    public CommonResult<List<MenuRespVO>> getChildMenus(@RequestParam("parentId") Long parentId) {
        List<MenuDO> menus = menuService.getChildMenus(parentId);
        return CommonResult.success(BeanUtils.toBean(menus, MenuRespVO.class));
    }

    /**
     * 获取菜单树
     *
     * @param domain 域
     * @return 菜单树
     */
    @GetMapping("/tree")
    public CommonResult<List<MenuTreeVO>> getMenuTree(@RequestParam("domain") Integer domain) {
        return CommonResult.success(menuService.getMenuTree(domain));
    }

    /**
     * 获取当前租户的菜单树
     *
     * @param domain 域
     * @return 菜单树
     */
    @GetMapping("/tree/current-tenant")
    public CommonResult<List<MenuTreeVO>> getCurrentTenantMenuTree(@RequestParam("domain") Integer domain) {
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        return CommonResult.success(menuService.getMenuTreeByTenant(tenantId, domain));
    }
}

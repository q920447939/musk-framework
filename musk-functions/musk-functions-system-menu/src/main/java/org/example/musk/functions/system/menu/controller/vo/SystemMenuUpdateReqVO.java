package org.example.musk.functions.system.menu.controller.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新菜单请求 VO
 *
 * @author musk-functions-menu
 */
@Data
public class SystemMenuUpdateReqVO {

    /**
     * ID
     */
    @NotNull(message = "菜单ID不能为空")
    private Long id;

    /**
     * 所属域(1:APP)
     */
    @NotNull(message = "所属域不能为空")
    private Integer domain;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    /**
     * 菜单层级(0表示首层)
     */
    @NotNull(message = "菜单层级不能为空")
    private Integer menuLevel;

    /**
     * 父菜单ID(如果层级为0,那么父菜单ID为null)
     */
    private Long parentMenuId;

    /**
     * 菜单关联ICON
     */
    @NotNull(message = "菜单关联ICON不能为空")
    private Integer menuRelationIconId;

    /**
     * 菜单ICON宽度
     */
    @NotNull(message = "菜单ICON宽度不能为空")
    private Double menuIconWidth;

    /**
     * 菜单ICON高度
     */
    @NotNull(message = "菜单ICON高度不能为空")
    private Double menuIconHeight;

    /**
     * 菜单点击跳转路径
     */
    @NotBlank(message = "菜单点击跳转路径不能为空")
    private String menuOnClickPath;

    /**
     * 携带参数(JSON格式)
     */
    private String params;
}

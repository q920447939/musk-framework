package org.example.musk.functions.menu.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜单响应 VO
 *
 * @author musk-functions-menu
 */
@Data
public class MenuRespVO {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 所属域(1:APP)
     */
    private Integer domain;
    
    /**
     * 菜单名称
     */
    private String menuName;
    
    /**
     * 菜单层级(0表示首层)
     */
    private Integer menuLevel;
    
    /**
     * 父菜单ID(如果层级为0,那么父菜单ID为null)
     */
    private Long parentMenuId;
    
    /**
     * 菜单关联ICON
     */
    private Integer menuRelationIconId;
    
    /**
     * 菜单ICON宽度
     */
    private Double menuIconWidth;
    
    /**
     * 菜单ICON高度
     */
    private Double menuIconHeight;
    
    /**
     * 菜单点击跳转路径
     */
    private String menuOnClickPath;
    
    /**
     * 携带参数(JSON格式)
     */
    private String params;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

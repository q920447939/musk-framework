package org.example.musk.functions.system.menu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.TenantBaseDO;

/**
 * 菜单表 DO
 *
 * @author musk-functions-menu
 */
@TableName("system_menu")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMenuDO extends TenantBaseDO {

    /**
     * ID
     */
    @TableId
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
}

package org.example.musk.functions.menu.controller.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 菜单树 VO
 *
 * @author musk-functions-menu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTreeVO extends MenuRespVO {
    
    /**
     * 子菜单列表
     */
    private List<MenuTreeVO> children;
}

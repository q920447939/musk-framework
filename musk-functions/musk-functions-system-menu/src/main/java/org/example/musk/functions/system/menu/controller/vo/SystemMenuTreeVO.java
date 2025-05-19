package org.example.musk.functions.system.menu.controller.vo;

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
public class SystemMenuTreeVO extends SystemMenuRespVO {

    /**
     * 子菜单列表
     */
    private List<SystemMenuTreeVO> children;
}

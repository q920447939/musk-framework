package org.example.musk.functions.system.menu.exception;


import org.example.musk.common.exception.BusinessException;

/**
 * 菜单异常类
 *
 * @author musk-functions-menu
 */
public class MenuException extends BusinessException {

    /**
     * 菜单不存在
     */
    public static final String MENU_NOT_EXISTS = "菜单不存在";

    /**
     * 菜单层级错误
     */
    public static final String MENU_LEVEL_ERROR = "菜单层级错误";

    /**
     * 父菜单不存在
     */
    public static final String PARENT_MENU_NOT_EXISTS = "父菜单不存在";

    /**
     * 菜单存在子菜单
     */
    public static final String MENU_HAS_CHILDREN = "菜单存在子菜单，不能删除";

    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public MenuException(String message) {
        super(message);
    }

}

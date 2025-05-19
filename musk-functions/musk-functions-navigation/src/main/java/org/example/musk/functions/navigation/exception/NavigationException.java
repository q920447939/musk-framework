package org.example.musk.functions.navigation.exception;

import org.example.musk.common.exception.BusinessException;

/**
 * 导航异常类
 *
 * @author musk-functions-navigation
 */
public class NavigationException extends BusinessException {

    /**
     * 导航不存在
     */
    public static final String NAVIGATION_NOT_EXISTS = "导航不存在";

    /**
     * 导航层级错误
     */
    public static final String INVALID_LEVEL = "导航层级错误";

    /**
     * 父导航不存在
     */
    public static final String PARENT_NOT_EXISTS = "父导航不存在";

    /**
     * 父导航ID必须为空
     */
    public static final String INVALID_PARENT_ID = "根导航的父导航ID必须为空";

    /**
     * 父导航ID不能为空
     */
    public static final String PARENT_ID_REQUIRED = "非根导航的父导航ID不能为空";

    /**
     * 导航存在子导航
     */
    public static final String NAVIGATION_HAS_CHILDREN = "导航存在子导航，不能删除";

    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public NavigationException(String message) {
        super(message);
    }
}

package org.example.musk.functions.icon.exception;

import org.example.musk.common.exception.ErrorCode;
import org.example.musk.common.exception.ServiceException;

/**
 * 图标模块异常
 *
 * @author musk-functions-icon
 */
public class IconException extends ServiceException {

    /**
     * 图标不存在
     */
    public static final ErrorCode ICON_NOT_EXISTS = new ErrorCode(1001001000, "图标不存在");

    /**
     * 图标编码已存在
     */
    public static final ErrorCode ICON_CODE_DUPLICATE = new ErrorCode(1001001001, "图标编码已存在");

    /**
     * 图标资源不存在
     */
    public static final ErrorCode ICON_RESOURCE_NOT_EXISTS = new ErrorCode(1001001002, "图标资源不存在");

    /**
     * 默认图标资源不能删除
     */
    public static final ErrorCode DEFAULT_RESOURCE_CANNOT_DELETE = new ErrorCode(1001001003, "默认图标资源不能删除");

    /**
     * 图标分类不存在
     */
    public static final ErrorCode CATEGORY_NOT_EXISTS = new ErrorCode(1001001004, "图标分类不存在");

    /**
     * 图标分类编码已存在
     */
    public static final ErrorCode CATEGORY_CODE_DUPLICATE = new ErrorCode(1001001005, "图标分类编码已存在");

    /**
     * 图标分类下存在图标，无法删除
     */
    public static final ErrorCode CATEGORY_HAS_ICONS = new ErrorCode(1001001006, "图标分类下存在图标，无法删除");

    /**
     * 图标分类下存在子分类，无法删除
     */
    public static final ErrorCode CATEGORY_HAS_CHILDREN = new ErrorCode(1001001007, "图标分类下存在子分类，无法删除");

    /**
     * 图标存在关联资源，无法删除
     */
    public static final ErrorCode ICON_HAS_RESOURCES = new ErrorCode(1001001008, "图标存在关联资源，无法删除");

    /**
     * 构造函数
     *
     * @param code 错误码
     */
    public IconException(ErrorCode code) {
        super(code);
    }

}

package org.example.musk.functions.resource.exception;

import org.example.musk.common.exception.BusinessException;

/**
 * 资源模块异常
 *
 * @author musk-functions-resource
 */
public class ResourceException extends BusinessException {

    /**
     * 文件类型不允许
     */
    public static final String FILE_TYPE_NOT_ALLOWED = "file.type.not.allowed";

    /**
     * 文件类型被禁止
     */
    public static final String FILE_TYPE_BLOCKED = "file.type.blocked";

    /**
     * 文件大小超过限制
     */
    public static final String FILE_SIZE_EXCEEDED = "file.size.exceeded";

    /**
     * 文件不存在
     */
    public static final String FILE_NOT_FOUND = "file.not.found";

    /**
     * 文件上传失败
     */
    public static final String FILE_UPLOAD_FAILED = "file.upload.failed";

    /**
     * 文件下载失败
     */
    public static final String FILE_DOWNLOAD_FAILED = "file.download.failed";

    /**
     * 文件删除失败
     */
    public static final String FILE_DELETE_FAILED = "file.delete.failed";
    /**
     * 文件不能为空
     */
    public static final String FILE_CANNOT_BE_EMPTY = "file.cannot.be.empty";

    /**
     * 资源不存在
     */
    public static final String RESOURCE_NOT_FOUND = "resource.not.found";

    /**
     * 资源分类不存在
     */
    public static final String CATEGORY_NOT_FOUND = "category.not.found";

    /**
     * 资源分类编码已存在
     */
    public static final String CATEGORY_CODE_DUPLICATE = "category.code.duplicate";

    /**
     * 资源编码已存在
     */
    public static final String RESOURCE_CODE_DUPLICATE = "resource.code.duplicate";

    /**
     * URL签名无效
     */
    public static final String URL_SIGNATURE_INVALID = "url.signature.invalid";

    /**
     * URL签名已过期
     */
    public static final String URL_SIGNATURE_EXPIRED = "url.signature.expired";

    /**
     * Referer不允许
     */
    public static final String REFERER_NOT_ALLOWED = "referer.not.allowed";

    /**
     * 存储空间不足
     */
    public static final String STORAGE_SPACE_INSUFFICIENT = "storage.space.insufficient";

    /**
     * 文件数量超过限制
     */
    public static final String FILE_COUNT_EXCEEDED = "file.count.exceeded";

    /**
     * 构造函数
     *
     * @param code 错误码
     */
    public ResourceException(String code) {
        super(code);
    }

    /**
     * 构造函数
     *
     * @param code 错误码
     * @param args 参数
     */
    public ResourceException(String code, Object... args) {
        super(code, args);
    }

    /**
     * 构造函数
     *
     * @param code  错误码
     * @param cause 原因
     */
    public ResourceException(String code, Throwable cause) {
        super(code, cause);
    }

    /**
     * 构造函数
     *
     * @param code  错误码
     * @param cause 原因
     * @param args  参数
     */
    public ResourceException(String code, Throwable cause, Object... args) {
        super(code, cause, args);
    }
}

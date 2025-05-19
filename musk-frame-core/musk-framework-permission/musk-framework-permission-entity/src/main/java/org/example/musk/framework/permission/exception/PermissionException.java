package org.example.musk.framework.permission.exception;

import org.example.musk.common.exception.BusinessException;

/**
 * 权限异常类
 *
 * @author musk-framework-permission
 */
public class PermissionException extends BusinessException {

    /**
     * 无操作权限
     */
    public static final String NO_PERMISSION = "没有执行此操作的权限";
    
    /**
     * 领域ID不存在
     */
    public static final String DOMAIN_ID_NOT_EXISTS = "领域ID不存在";
    
    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public PermissionException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     *
     * @param code 错误码
     * @param message 错误信息
     */
    public PermissionException(String code, String message) {
        super(code, message);
    }
}

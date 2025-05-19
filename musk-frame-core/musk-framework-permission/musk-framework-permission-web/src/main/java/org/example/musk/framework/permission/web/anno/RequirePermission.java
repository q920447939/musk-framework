package org.example.musk.framework.permission.web.anno;

import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要权限注解
 * 
 * 用于标记需要进行权限检查的方法
 *
 * @author musk-framework-permission
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    
    /**
     * 资源类型
     */
    ResourceTypeEnum resourceType();
    
    /**
     * 资源ID
     * 为空字符串时表示从方法参数中获取
     */
    String resourceId() default "";
    
    /**
     * 资源ID参数名
     * 当resourceId为空字符串时，从方法参数中获取此参数名对应的值作为resourceId
     */
    String resourceIdParam() default "id";
    
    /**
     * 操作类型
     */
    OperationTypeEnum operationType();
}

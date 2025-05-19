package org.example.musk.framework.permission.web.aop;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.framework.permission.enums.OperationTypeEnum;
import org.example.musk.framework.permission.enums.ResourceTypeEnum;
import org.example.musk.framework.permission.exception.PermissionException;
import org.example.musk.framework.permission.service.DomainPermissionService;
import org.example.musk.framework.permission.web.anno.RequirePermission;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 领域权限切面
 *
 * 用于拦截需要进行权限检查的方法
 *
 * @author musk-framework-permission
 */
@Aspect
@Component
@Order(90) // 确保在事务切面之后执行
@Slf4j
public class DomainPermissionAspect {

    @Resource
    private DomainPermissionService domainPermissionService;

    /**
     * 定义切点 - 拦截所有控制器方法
     */
    @Pointcut("execution(* org.example.musk.functions.*.controller..*.*(..))")
    public void controllerPointcut() {}

    /**
     * 定义切点 - 拦截带有RequirePermission注解的方法
     */
    @Pointcut("@annotation(org.example.musk.framework.permission.web.anno.RequirePermission)")
    public void requirePermissionPointcut() {}

    /**
     * 拦截所有控制器方法
     */
    @Before("controllerPointcut()")
    public void checkControllerPermission(JoinPoint joinPoint) {
        // 获取当前租户和领域信息
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        if (tenantId == null || domainId == null) {
            log.warn("当前上下文中未找到租户ID或领域ID");
            throw new PermissionException(PermissionException.DOMAIN_ID_NOT_EXISTS);
        }

        // 获取目标方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();

        // 检查是否有RequirePermission注解
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);
        if (annotation != null) {
            // 如果有注解，则由requirePermissionPointcut处理
            return;
        }

        // 确定资源类型（基于包名）
        String resourceType = determineResourceType(targetClass);
        if (resourceType == null) {
            // 无法确定资源类型，但为了安全起见，不应该跳过权限检查
            log.warn("无法确定资源类型，为安全起见拒绝访问: {}", targetClass.getName());
            throw new PermissionException("无法确定资源类型,拒绝访问");
        }

        // 确定操作类型（基于HTTP方法）
        String operationType = determineOperationType(method);

        // 确定资源ID（可能基于方法参数）
        String resourceId = determineResourceId(joinPoint);

        // 检查权限
        boolean hasPermission = domainPermissionService.hasPermission(
            tenantId, domainId, resourceType, resourceId, operationType);

        if (!hasPermission) {
            log.warn("域 {} 没有权限执行 {} 操作，资源类型：{}，资源ID：{}",
                    domainId, operationType, resourceType, resourceId);
            throw new PermissionException(PermissionException.NO_PERMISSION);
        }
    }

    /**
     * 拦截带有RequirePermission注解的方法
     */
    @Before("requirePermissionPointcut()")
    public void checkAnnotatedPermission(JoinPoint joinPoint) {
        // 获取当前租户和领域信息
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        if (tenantId == null || domainId == null) {
            log.warn("当前上下文中未找到租户ID或领域ID");
            throw new PermissionException(PermissionException.DOMAIN_ID_NOT_EXISTS);
        }

        // 获取目标方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取注解
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);
        if (annotation == null) {
            return;
        }

        // 获取资源类型和操作类型
        ResourceTypeEnum resourceType = annotation.resourceType();
        OperationTypeEnum operationType = annotation.operationType();

        // 获取资源ID
        String resourceId = annotation.resourceId();
        if (resourceId.isEmpty()) {
            // 从方法参数中获取资源ID
            resourceId = getResourceIdFromParams(joinPoint, method, annotation.resourceIdParam());
        }

        // 检查权限
        boolean hasPermission = domainPermissionService.hasPermission(
            tenantId, domainId, resourceType, resourceId, operationType);

        if (!hasPermission) {
            log.warn("域 {} 没有权限执行 {} 操作，资源类型：{}，资源ID：{}",
                    domainId, operationType.getCode(), resourceType.getCode(), resourceId);
            throw new PermissionException(PermissionException.NO_PERMISSION);
        }
    }

    /**
     * 确定资源类型
     */
    private String determineResourceType(Class<?> targetClass) {
        // 基于包名确定资源类型
        String packageName = targetClass.getPackage().getName();
        if (packageName.contains("menu")) {
            return ResourceTypeEnum.MENU.getCode();
        } else if (packageName.contains("system.params")) {
            return ResourceTypeEnum.SYSTEM_PARAMS.getCode();
        }

        // 无法确定资源类型
        return null;
    }

    /**
     * 确定操作类型
     */
    private String determineOperationType(Method method) {
        // 基于HTTP方法确定操作类型
        if (method.isAnnotationPresent(GetMapping.class)) {
            return OperationTypeEnum.READ.getCode();
        }  else if (method.isAnnotationPresent(PutMapping.class)) {
            return OperationTypeEnum.UPDATE.getCode();
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return OperationTypeEnum.DELETE.getCode();
        }

        // 基于方法名确定操作类型
        String methodName = method.getName();
        if (methodName.startsWith("create") || methodName.startsWith("add") || methodName.startsWith("save")) {
            return OperationTypeEnum.CREATE.getCode();
        } else if (methodName.startsWith("update") || methodName.startsWith("modify")) {
            return OperationTypeEnum.UPDATE.getCode();
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return OperationTypeEnum.DELETE.getCode();
        } else if (methodName.startsWith("get") || methodName.startsWith("query") ||
                  methodName.startsWith("find") || methodName.startsWith("list")) {
            return OperationTypeEnum.READ.getCode();
        }

        // 默认为READ
        return OperationTypeEnum.READ.getCode();
    }

    /**
     * 确定资源ID
     */
    private String determineResourceId(JoinPoint joinPoint) {
        // 简化处理，返回null表示整个资源类型
        return null;
    }

    /**
     * 从方法参数中获取资源ID
     */
    private String getResourceIdFromParams(JoinPoint joinPoint, Method method, String paramName) {
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        // 创建参数名到参数值的映射
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i < parameters.length && i < args.length; i++) {
            paramMap.put(parameters[i].getName(), args[i]);
        }

        // 查找指定参数名的值
        Object value = paramMap.get(paramName);
        return value != null ? value.toString() : null;
    }
}

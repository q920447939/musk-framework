package org.example.musk.functions.resource.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.resource.dao.ResourceOperationLogMapper;
import org.example.musk.functions.resource.entity.SystemResourceOperationLogDO;
import org.example.musk.functions.resource.enums.OperatorTypeEnum;
import org.example.musk.functions.resource.service.ResourceLogService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源日志服务实现
 *
 * @author musk-functions-resource
 */
@Service
@Slf4j
@DS(DBConstant.SYSTEM)
public class ResourceLogServiceImpl implements ResourceLogService {

    @Resource
    private ResourceOperationLogMapper resourceOperationLogMapper;

    @Override
    public Integer logResourceOperation(Integer resourceId, Integer operationType, boolean operationResult, String errorMessage) {
        try {
            // 获取当前请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String operationIp = "";
            String operationUa = "";
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                operationIp = getClientIp(request);
                operationUa = request.getHeader("User-Agent");
            }

            // 创建日志记录
            SystemResourceOperationLogDO log = new SystemResourceOperationLogDO();
            log.setResourceId(resourceId);
            log.setOperationType(operationType);
            log.setOperationResult(operationResult ? 0 : 1);
            log.setErrorMessage(errorMessage);
            log.setOperationIp(operationIp);
            log.setOperationUa(operationUa);
            log.setOperationTime(LocalDateTime.now());
            log.setTenantId(ThreadLocalTenantContext.getTenantId());
            log.setDomainId(ThreadLocalTenantContext.getDomainId());

            // 设置操作人信息
            Integer memberId = ThreadLocalTenantContext.getMemberId();
            if (memberId != null) {
                log.setOperatorId(memberId);
                log.setOperatorType(OperatorTypeEnum.MEMBER.getCode());
            } else {
                // 如果没有会员ID，可能是系统用户
                // 这里可以根据实际情况获取系统用户ID
                log.setOperatorId(0); // 临时使用0表示系统用户
                log.setOperatorType(OperatorTypeEnum.SYSTEM_USER.getCode());
            }

            // 保存日志
            resourceOperationLogMapper.insert(log);
            return log.getId();
        } catch (Exception e) {
            // 记录日志失败不应影响主流程
            log.error("记录资源操作日志失败", e);
            return null;
        }
    }

    @Override
    public List<SystemResourceOperationLogDO> getResourceOperationLogs(Integer resourceId) {
        LambdaQueryWrapper<SystemResourceOperationLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceOperationLogDO::getResourceId, resourceId);
        queryWrapper.eq(SystemResourceOperationLogDO::getTenantId, ThreadLocalTenantContext.getTenantId());
        queryWrapper.eq(SystemResourceOperationLogDO::getDomainId, ThreadLocalTenantContext.getDomainId());
        queryWrapper.orderByDesc(SystemResourceOperationLogDO::getOperationTime);
        return resourceOperationLogMapper.selectList(queryWrapper);
    }

    @Override
    public List<SystemResourceOperationLogDO> getUserOperationLogs(Integer operatorId, Integer operatorType) {
        LambdaQueryWrapper<SystemResourceOperationLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceOperationLogDO::getOperatorId, operatorId);
        queryWrapper.eq(SystemResourceOperationLogDO::getOperatorType, operatorType);
        queryWrapper.eq(SystemResourceOperationLogDO::getTenantId, ThreadLocalTenantContext.getTenantId());
        queryWrapper.eq(SystemResourceOperationLogDO::getDomainId, ThreadLocalTenantContext.getDomainId());
        queryWrapper.orderByDesc(SystemResourceOperationLogDO::getOperationTime);
        return resourceOperationLogMapper.selectList(queryWrapper);
    }

    @Override
    public int countResourceOperations(Integer resourceId, Integer operationType) {
        LambdaQueryWrapper<SystemResourceOperationLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceOperationLogDO::getResourceId, resourceId);
        queryWrapper.eq(SystemResourceOperationLogDO::getOperationType, operationType);
        queryWrapper.eq(SystemResourceOperationLogDO::getOperationResult, 0); // 成功的操作
        queryWrapper.eq(SystemResourceOperationLogDO::getTenantId, ThreadLocalTenantContext.getTenantId());
        queryWrapper.eq(SystemResourceOperationLogDO::getDomainId, ThreadLocalTenantContext.getDomainId());
        return resourceOperationLogMapper.selectCount(queryWrapper).intValue();
    }

    @Override
    public int countUserOperations(Integer operatorId, Integer operatorType, Integer operationType) {
        LambdaQueryWrapper<SystemResourceOperationLogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemResourceOperationLogDO::getOperatorId, operatorId);
        queryWrapper.eq(SystemResourceOperationLogDO::getOperatorType, operatorType);
        queryWrapper.eq(SystemResourceOperationLogDO::getOperationType, operationType);
        queryWrapper.eq(SystemResourceOperationLogDO::getOperationResult, 0); // 成功的操作
        queryWrapper.eq(SystemResourceOperationLogDO::getTenantId, ThreadLocalTenantContext.getTenantId());
        queryWrapper.eq(SystemResourceOperationLogDO::getDomainId, ThreadLocalTenantContext.getDomainId());
        return resourceOperationLogMapper.selectCount(queryWrapper).intValue();
    }

    /**
     * 获取客户端IP地址
     *
     * @param request 请求
     * @return IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

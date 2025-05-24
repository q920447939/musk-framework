package org.example.musk.functions.resource.service;

import org.example.musk.functions.resource.entity.SystemResourceOperationLogDO;

import java.util.List;

/**
 * 资源日志服务接口
 *
 * @author musk-functions-resource
 */
public interface ResourceLogService {

    /**
     * 记录资源操作日志
     *
     * @param resourceId      资源ID
     * @param operationType   操作类型
     * @param operationResult 操作结果
     * @param errorMessage    错误信息
     * @return 日志ID
     */
    Integer logResourceOperation(Integer resourceId, Integer operationType, boolean operationResult, String errorMessage);

    /**
     * 查询资源操作日志
     *
     * @param resourceId 资源ID
     * @return 操作日志列表
     */
    List<SystemResourceOperationLogDO> getResourceOperationLogs(Integer resourceId);

    /**
     * 查询用户操作日志
     *
     * @param operatorId   操作人ID
     * @param operatorType 操作人类型
     * @return 操作日志列表
     */
    List<SystemResourceOperationLogDO> getUserOperationLogs(Integer operatorId, Integer operatorType);

    /**
     * 统计资源操作次数
     *
     * @param resourceId    资源ID
     * @param operationType 操作类型
     * @return 操作次数
     */
    int countResourceOperations(Integer resourceId, Integer operationType);

    /**
     * 统计用户操作次数
     *
     * @param operatorId    操作人ID
     * @param operatorType  操作人类型
     * @param operationType 操作类型
     * @return 操作次数
     */
    int countUserOperations(Integer operatorId, Integer operatorType, Integer operationType);
}

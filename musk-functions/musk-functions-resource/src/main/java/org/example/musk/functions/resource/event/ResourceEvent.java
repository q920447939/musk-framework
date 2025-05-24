package org.example.musk.functions.resource.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资源事件基类
 *
 * @author musk-functions-resource
 */
@Data
public abstract class ResourceEvent {

    /**
     * 资源ID
     */
    private Integer resourceId;

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 域ID
     */
    private Integer domainId;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 操作人类型
     */
    private Integer operatorType;

    /**
     * 构造函数
     */
    public ResourceEvent() {
        this.eventTime = LocalDateTime.now();
    }

    /**
     * 构造函数
     *
     * @param resourceId   资源ID
     * @param tenantId     租户ID
     * @param domainId     域ID
     * @param operatorId   操作人ID
     * @param operatorType 操作人类型
     */
    public ResourceEvent(Integer resourceId, Integer tenantId, Integer domainId, Integer operatorId, Integer operatorType) {
        this.resourceId = resourceId;
        this.tenantId = tenantId;
        this.domainId = domainId;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.eventTime = LocalDateTime.now();
    }
}

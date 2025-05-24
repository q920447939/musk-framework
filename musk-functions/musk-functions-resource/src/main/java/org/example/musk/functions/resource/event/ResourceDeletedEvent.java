package org.example.musk.functions.resource.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源删除事件
 *
 * @author musk-functions-resource
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceDeletedEvent extends ResourceEvent {

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型
     */
    private Integer resourceType;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 构造函数
     */
    public ResourceDeletedEvent() {
        super();
    }

    /**
     * 构造函数
     *
     * @param resourceId   资源ID
     * @param tenantId     租户ID
     * @param domainId     域ID
     * @param operatorId   操作人ID
     * @param operatorType 操作人类型
     * @param resourceName 资源名称
     * @param resourceType 资源类型
     * @param fileType     文件类型
     */
    public ResourceDeletedEvent(Integer resourceId, Integer tenantId, Integer domainId, Integer operatorId, Integer operatorType,
                              String resourceName, Integer resourceType, String fileType) {
        super(resourceId, tenantId, domainId, operatorId, operatorType);
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.fileType = fileType;
    }
}

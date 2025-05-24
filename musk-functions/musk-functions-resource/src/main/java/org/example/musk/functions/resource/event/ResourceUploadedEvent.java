package org.example.musk.functions.resource.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源上传事件
 *
 * @author musk-functions-resource
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceUploadedEvent extends ResourceEvent {

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
     * 文件大小
     */
    private Long fileSize;

    /**
     * 存储类型
     */
    private Integer storageType;

    /**
     * 访问URL
     */
    private String accessUrl;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 标签
     */
    private String tags;

    /**
     * 构造函数
     */
    public ResourceUploadedEvent() {
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
     * @param fileSize     文件大小
     * @param storageType  存储类型
     * @param accessUrl    访问URL
     * @param categoryId   分类ID
     * @param tags         标签
     */
    public ResourceUploadedEvent(Integer resourceId, Integer tenantId, Integer domainId, Integer operatorId, Integer operatorType,
                                String resourceName, Integer resourceType, String fileType, Long fileSize,
                                Integer storageType, String accessUrl, Integer categoryId, String tags) {
        super(resourceId, tenantId, domainId, operatorId, operatorType);
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.storageType = storageType;
        this.accessUrl = accessUrl;
        this.categoryId = categoryId;
        this.tags = tags;
    }
}

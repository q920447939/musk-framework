package org.example.musk.functions.resource.vo;

import lombok.Data;

/**
 * 资源上传响应VO
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceUploadRespVO {

    /**
     * 资源ID
     */
    private Integer resourceId;

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
     * 访问URL
     */
    private String accessUrl;
}

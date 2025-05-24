package org.example.musk.functions.resource.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资源响应VO
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceRespVO {

    /**
     * 资源ID
     */
    private Integer id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源编码
     */
    private String resourceCode;

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
     * 状态
     */
    private Integer status;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标签
     */
    private String tags;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新者
     */
    private String updater;
}

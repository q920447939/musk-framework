package org.example.musk.functions.resource.vo;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 资源更新请求VO
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceUpdateReqVO {

    /**
     * 资源ID
     */
    @NotNull(message = "资源ID不能为空")
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
     * 状态
     */
    private Integer status;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 标签
     */
    private String tags;

    /**
     * 描述
     */
    private String description;
}

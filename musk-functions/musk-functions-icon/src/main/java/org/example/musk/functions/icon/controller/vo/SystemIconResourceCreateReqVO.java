package org.example.musk.functions.icon.controller.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建图标资源请求 VO
 *
 * @author musk-functions-icon
 */
@Data
public class SystemIconResourceCreateReqVO {

    /**
     * 图标ID
     */
    @NotNull(message = "图标ID不能为空")
    private Integer iconId;

    /**
     * 平台类型（1:APP 2:WEB 3:通用）
     */
    @NotNull(message = "平台类型不能为空")
    private Integer platformType;

    /**
     * 资源类型（1:URL 2:Base64 3:字体图标）
     */
    @NotNull(message = "资源类型不能为空")
    private Integer resourceType;

    /**
     * 资源URL
     */
    @Size(max = 500, message = "资源URL长度不能超过500")
    private String resourceUrl;

    /**
     * 资源内容（Base64或字体图标代码）
     */
    private String resourceContent;

    /**
     * 宽度（像素）
     */
    private Integer width;

    /**
     * 高度（像素）
     */
    private Integer height;

    /**
     * 版本号
     */
    @Size(max = 20, message = "版本号长度不能超过20")
    private String version;

    /**
     * 是否默认资源
     */
    @NotNull(message = "是否默认资源不能为空")
    private Boolean isDefault;
}

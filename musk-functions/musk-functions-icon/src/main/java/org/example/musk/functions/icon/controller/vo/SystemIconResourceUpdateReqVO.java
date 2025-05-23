package org.example.musk.functions.icon.controller.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新图标资源请求 VO
 *
 * @author musk-functions-icon
 */
@Data
public class SystemIconResourceUpdateReqVO {

    /**
     * 资源ID
     */
    @NotNull(message = "资源ID不能为空")
    private Integer id;

    /**
     * 图标ID
     */
    @NotNull(message = "图标ID不能为空")
    private Integer iconId;

    /**
     * 域ID
     */
    @NotNull(message = "域ID不能为空")
    private Integer domainId;

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

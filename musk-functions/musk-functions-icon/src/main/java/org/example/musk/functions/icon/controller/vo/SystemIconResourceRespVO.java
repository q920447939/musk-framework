package org.example.musk.functions.icon.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 图标资源响应 VO
 *
 * @author musk-functions-icon
 */
@Data
public class SystemIconResourceRespVO {

    /**
     * 资源ID
     */
    private Integer id;

    /**
     * 图标ID
     */
    private Integer iconId;

    /**
     * 域ID
     */
    private Integer domainId;

    /**
     * 资源类型（1:URL 2:Base64 3:字体图标）
     */
    private Integer resourceType;

    /**
     * 资源URL
     */
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
    private String version;

    /**
     * 是否默认资源
     */
    private Boolean isDefault;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

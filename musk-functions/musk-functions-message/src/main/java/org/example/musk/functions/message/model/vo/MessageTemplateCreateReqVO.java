package org.example.musk.functions.message.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建消息模板请求VO
 *
 * @author musk-functions-message
 */
@Data
public class MessageTemplateCreateReqVO {

    private Integer tenantId;
    /**
     * 域ID
     */
    private Integer domainId;

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空")
    @Size(max = 64, message = "模板编码长度不能超过64个字符")
    private String templateCode;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 128, message = "模板名称长度不能超过128个字符")
    private String templateName;

    /**
     * 模板类型(1:文本消息 2:图文消息 3:系统通知 4:更新通知)
     */
    @NotNull(message = "模板类型不能为空")
    private Integer templateType;

    /**
     * 标题模板
     */
    @NotBlank(message = "标题模板不能为空")
    @Size(max = 255, message = "标题模板长度不能超过255个字符")
    private String titleTemplate;

    /**
     * 内容模板
     */
    @NotBlank(message = "内容模板不能为空")
    private String contentTemplate;

    /**
     * 图片URL(图文消息)
     */
    @Size(max = 255, message = "图片URL长度不能超过255个字符")
    private String imageUrl;

    /**
     * 优先级(0:普通 1:重要 2:紧急)
     */
    private Integer priority;

    /**
     * 是否强制消息
     */
    private Boolean isForced;

    /**
     * 操作类型(0:无操作 1:跳转链接 2:打开应用 3:下载更新)
     */
    private Integer actionType;

    /**
     * 操作URL
     */
    @Size(max = 255, message = "操作URL长度不能超过255个字符")
    private String actionUrl;

    /**
     * 操作参数(JSON格式)
     */
    @Size(max = 255, message = "操作参数长度不能超过255个字符")
    private String actionParams;

    /**
     * 平台类型(1:APP 2:WEB 3:全平台)
     */
    @NotNull(message = "平台类型不能为空")
    private Integer platformType;

    /**
     * 状态(0:禁用 1:启用)
     */
    private Integer status;


    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}

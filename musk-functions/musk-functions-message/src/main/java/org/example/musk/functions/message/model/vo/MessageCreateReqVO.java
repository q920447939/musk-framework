package org.example.musk.functions.message.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建消息请求VO
 *
 * @author musk-functions-message
 */
@Data
public class MessageCreateReqVO {

    /**
     * 域ID
     */
    private Integer domainId;

    /**
     * 消息类型(1:文本消息 2:图文消息 3:系统通知 4:更新通知)
     */
    @NotNull(message = "消息类型不能为空")
    private Integer messageType;

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空")
    @Size(max = 255, message = "消息标题长度不能超过255个字符")
    private String title;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

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
     * 生效开始时间
     */
    private LocalDateTime startTime;

    /**
     * 生效结束时间
     */
    private LocalDateTime endTime;

    /**
     * 平台类型(1:APP 2:WEB 3:全平台)
     */
    @NotNull(message = "平台类型不能为空")
    private Integer platformType;

    /**
     * 消息模板ID
     */
    private Integer templateId;
    private Integer tenantId;

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getTenantId() {
        return tenantId;
    }
}

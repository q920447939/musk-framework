package org.example.musk.functions.message.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.BaseDO;

/**
 * 消息模板表 DO
 *
 * @author musk-functions-message
 */
@TableName("system_message_template")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessageTemplateDO extends BaseDO {

    /**
     * 模板ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 域ID
     */
    @TableField("domain_id")
    private Integer domainId;

    /**
     * 模板编码
     */
    @TableField("template_code")
    private String templateCode;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 模板类型(1:文本消息 2:图文消息 3:系统通知 4:更新通知)
     */
    @TableField("template_type")
    private Integer templateType;

    /**
     * 标题模板
     */
    @TableField("title_template")
    private String titleTemplate;

    /**
     * 内容模板
     */
    @TableField("content_template")
    private String contentTemplate;

    /**
     * 图片URL(图文消息)
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 优先级(0:普通 1:重要 2:紧急)
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 是否强制消息
     */
    @TableField("is_forced")
    private Boolean isForced;

    /**
     * 操作类型(0:无操作 1:跳转链接 2:打开应用 3:下载更新)
     */
    @TableField("action_type")
    private Integer actionType;

    /**
     * 操作URL
     */
    @TableField("action_url")
    private String actionUrl;

    /**
     * 操作参数(JSON格式)
     */
    @TableField("action_params")
    private String actionParams;

    /**
     * 平台类型(1:APP 2:WEB 3:全平台)
     */
    @TableField("platform_type")
    private Integer platformType;

    /**
     * 状态(0:禁用 1:启用)
     */
    @TableField("status")
    private Integer status;
}

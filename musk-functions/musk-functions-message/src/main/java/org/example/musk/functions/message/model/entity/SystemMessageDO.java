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
import org.example.musk.common.pojo.db.DomainBaseDO;

import java.time.LocalDateTime;

/**
 * 消息主表 DO
 *
 * @author musk-functions-message
 */
@TableName("system_message")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessageDO extends DomainBaseDO {

    /**
     * 消息ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 消息类型(1:文本消息 2:图文消息 3:系统通知 4:更新通知)
     */
    @TableField("message_type")
    private Integer messageType;

    /**
     * 消息标题
     */
    @TableField("title")
    private String title;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

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
     * 生效开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 生效结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 状态(0:草稿 1:已发布 2:已过期)
     */
    @TableField("status")
    private Integer status;

    /**
     * 平台类型(1:APP 2:WEB 3:全平台)
     */
    @TableField("platform_type")
    private Integer platformType;

    /**
     * 消息模板ID
     */
    @TableField("template_id")
    private Integer templateId;
}

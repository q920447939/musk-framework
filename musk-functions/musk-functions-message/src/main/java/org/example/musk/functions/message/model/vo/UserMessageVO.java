package org.example.musk.functions.message.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户消息视图对象
 *
 * @author musk-functions-message
 */
@Data
public class UserMessageVO {

    /**
     * 消息ID
     */
    private Integer messageId;

    /**
     * 消息类型(1:文本消息 2:图文消息 3:系统通知 4:更新通知)
     */
    private Integer messageType;

    /**
     * 消息类型描述
     */
    private String messageTypeDesc;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容摘要
     */
    private String summary;

    /**
     * 图片URL(图文消息)
     */
    private String imageUrl;

    /**
     * 优先级(0:普通 1:重要 2:紧急)
     */
    private Integer priority;

    /**
     * 优先级描述
     */
    private String priorityDesc;

    /**
     * 是否强制消息
     */
    private Boolean isForced;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 消息创建时间
     */
    private LocalDateTime createTime;

    /**
     * 操作类型(0:无操作 1:跳转链接 2:打开应用 3:下载更新)
     */
    private Integer actionType;

    /**
     * 操作类型描述
     */
    private String actionTypeDesc;
}

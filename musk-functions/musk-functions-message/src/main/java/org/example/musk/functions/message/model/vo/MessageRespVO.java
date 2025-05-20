package org.example.musk.functions.message.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息响应VO
 *
 * @author musk-functions-message
 */
@Data
public class MessageRespVO {

    /**
     * 消息ID
     */
    private Integer id;

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
     * 消息内容
     */
    private String content;

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
     * 操作类型(0:无操作 1:跳转链接 2:打开应用 3:下载更新)
     */
    private Integer actionType;

    /**
     * 操作类型描述
     */
    private String actionTypeDesc;

    /**
     * 操作URL
     */
    private String actionUrl;

    /**
     * 操作参数(JSON格式)
     */
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
     * 状态(0:草稿 1:已发布 2:已过期)
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 平台类型(1:APP 2:WEB 3:全平台)
     */
    private Integer platformType;

    /**
     * 平台类型描述
     */
    private String platformTypeDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

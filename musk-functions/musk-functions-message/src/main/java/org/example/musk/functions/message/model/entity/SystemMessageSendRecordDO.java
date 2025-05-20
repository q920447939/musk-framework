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

import java.time.LocalDateTime;

/**
 * 消息发送记录表 DO
 *
 * @author musk-functions-message
 */
@TableName("system_message_send_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMessageSendRecordDO extends BaseDO {

    /**
     * 记录ID
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
     * 消息ID
     */
    @TableField("message_id")
    private Integer messageId;

    /**
     * 目标类型(1:单个用户 2:用户组 3:全部用户)
     */
    @TableField("target_type")
    private Integer targetType;

    /**
     * 目标ID(用户ID或用户组ID)
     */
    @TableField("target_id")
    private String targetId;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private LocalDateTime sendTime;

    /**
     * 发送状态(0:待发送 1:发送中 2:发送成功 3:发送失败)
     */
    @TableField("send_status")
    private Integer sendStatus;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;
}

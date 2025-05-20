package org.example.musk.functions.message.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 发送消息请求VO
 *
 * @author musk-functions-message
 */
@Data
public class MessageSendReqVO {

    /**
     * 消息ID
     */
    @NotNull(message = "消息ID不能为空")
    private Integer messageId;

    /**
     * 目标类型(1:单个用户 2:用户组 3:全部用户)
     */
    @NotNull(message = "目标类型不能为空")
    private Integer targetType;

    /**
     * 目标ID列表(用户ID或用户组ID)
     */
    private List<Integer> targetIds;
}

package org.example.musk.functions.message.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 使用模板发送消息请求VO
 *
 * @author musk-functions-message
 */
@Data
public class MessageTemplateSendReqVO {

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    /**
     * 模板参数
     */
    private Map<String, Object> templateParams;

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

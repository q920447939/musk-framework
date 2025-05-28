package org.example.musk.functions.invitation.web.vo.request;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 邀请码创建请求VO
 *
 * @author musk-functions-member-invitation
 */
@Data
public class InvitationCodeCreateReqVO {

    /**
     * 邀请码类型(1:个人邀请码 2:活动邀请码)
     */
    @NotNull(message = "邀请码类型不能为空")
    @Min(value = 1, message = "邀请码类型无效")
    @Max(value = 2, message = "邀请码类型无效")
    private Integer codeType;

    /**
     * 最大使用次数(null表示无限制)
     */
    @Min(value = 1, message = "最大使用次数必须大于0")
    private Integer maxUseCount;

    /**
     * 过期小时数(null表示永不过期)
     */
    @Min(value = 1, message = "过期小时数必须大于0")
    private Integer expireHours;

}

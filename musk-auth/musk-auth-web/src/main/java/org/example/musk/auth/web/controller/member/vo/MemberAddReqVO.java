package org.example.musk.auth.web.controller.member.vo;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class MemberAddReqVO {

    @NotNull(message = "注册渠道不能为空")
    private Integer registerChannel;

    @NotBlank(message = "会员账号不能为空")
    private String memberCode;

    @NotBlank(message = "会员昵称不能为空")
    private String memberNickName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotNull(message = "会员识别id不能为空")
    private Integer memberSimpleId;

    @NotNull(message = "邀请会员识别id不能为空")
    private Integer inviteMemberSimpleId;

    @NotBlank(message = "头像地址不能为空")
    private String avatar;

    @NotNull(message = "状态枚举不能为空")
    private Integer status;

}

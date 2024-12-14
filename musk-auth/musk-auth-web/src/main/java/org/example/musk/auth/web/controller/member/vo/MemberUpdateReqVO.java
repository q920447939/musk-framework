package org.example.musk.auth.web.controller.member.vo;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class MemberUpdateReqVO {

    @NotNull(message = "编号不能为空")
    private Integer id;

    private Integer registerChannel;

    private String memberCode;

    private String memberNickName;

    private String password;

    private Integer memberSimpleId;

    private Integer inviteMemberSimpleId;

    private String avatar;

    private Integer status;

}
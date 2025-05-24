package org.example.musk.auth.web.controller.member.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberRespVO {

    private Integer id;

    private Integer registerChannel;

    private String memberCode;

    private String memberNickName;


    private Integer memberSimpleId;

    private Integer inviteMemberSimpleId;

    private String avatar;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime createTime;


}

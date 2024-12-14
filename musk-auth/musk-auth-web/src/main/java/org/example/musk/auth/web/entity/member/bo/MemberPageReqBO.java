package org.example.musk.auth.web.entity.member.bo;

import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberPageReqBO extends PageParam {

    private Integer registerChannel;

    private String memberCode;

    private String memberNickName;

    private String password;

    private Integer memberSimpleId;

    private Integer inviteMemberSimpleId;

    private String avatar;

    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

}
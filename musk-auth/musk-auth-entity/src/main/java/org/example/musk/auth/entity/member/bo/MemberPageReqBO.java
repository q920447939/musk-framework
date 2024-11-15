package org.example.musk.auth.entity.member.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@ToString(callSuper = true)
public class MemberPageReqBO {


    private Integer registerChannel;

    private String memberCode;

    private String memberNickName;

    private String password;

    private Integer memberSimpleId;

    private Integer inviteMemberSimpleId;

    private String avatar;

    private Integer status;

    private LocalDateTime[] createTime;

}

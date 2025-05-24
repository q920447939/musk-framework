package org.example.musk.auth.entity.member.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@ToString(callSuper = true)
public class MemberPageReqVO {


    private String memberCode;

    private Integer registerChannel;

    private String memberNickName;

    private LocalDateTime[] createTime;

}

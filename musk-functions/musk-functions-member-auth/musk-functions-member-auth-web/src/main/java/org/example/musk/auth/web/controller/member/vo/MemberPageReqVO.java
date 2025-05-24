package org.example.musk.auth.web.controller.member.vo;


import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import static org.example.musk.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MemberPageReqVO extends PageParam {

    private Integer registerChannel;

    private String memberCode;

    private String memberNickName;

    private String password;

    private Integer memberSimpleId;

    private Integer inviteMemberSimpleId;

    private String avatar;

    private Integer status;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

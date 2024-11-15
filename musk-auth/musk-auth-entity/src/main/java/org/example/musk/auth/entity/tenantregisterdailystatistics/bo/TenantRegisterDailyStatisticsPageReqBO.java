package org.example.musk.auth.entity.tenantregisterdailystatistics.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@ToString(callSuper = true)
public class TenantRegisterDailyStatisticsPageReqBO {



    /**
     注册日期
     */
    private LocalDate[] registerDate;

    private Integer num;


    /**
     创建时间
     */
    private LocalDateTime[] createTime;

}

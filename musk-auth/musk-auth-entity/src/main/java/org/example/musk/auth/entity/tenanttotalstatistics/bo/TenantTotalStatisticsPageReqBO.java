package org.example.musk.auth.entity.tenanttotalstatistics.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@ToString(callSuper = true)
public class TenantTotalStatisticsPageReqBO  {


    private Integer totalMember;

    private Integer totalRealNameMember;

    private BigDecimal totalInvestAmount;

    private Integer totalInvestTimes;

    private BigDecimal totalCashAmount;


    /**
     创建时间
     */
    private LocalDateTime[] createTime;

}

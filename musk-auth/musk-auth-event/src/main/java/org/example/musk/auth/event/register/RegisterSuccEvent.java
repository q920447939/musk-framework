package org.example.musk.auth.event.register;


import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.event.register.entity.RegisterSuccInfo;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.service.core.statistics.tenantregisterdailystatistics.TenantRegisterDailyStatisticsService;
import org.example.musk.auth.service.core.statistics.tenanttotalstatistics.TenantTotalStatisticsService;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
public class RegisterSuccEvent {


    @Resource
    private MemberService memberService;

    @Resource
    private TenantRegisterDailyStatisticsService tenantRegisterDailyStatisticsService;



    @Resource
    private TenantTotalStatisticsService tenantTotalStatisticsService;




    private static final BigDecimal ZERO = BigDecimal.ZERO;



    @EventListener
    @Async
    public void registerStatistics(@NonNull RegisterSuccInfo importSuccEndEvent) {
        MemberDO memberDO = importSuccEndEvent.getMemberDO();
        Integer memberId = memberDO.getId();
        Integer tenantId = ThreadLocalTenantContext.getTenantId();
        tenantRegisterDailyStatisticsService.saveTenantRegisterDailyStatistics(tenantId, LocalDate.now());
        tenantTotalStatisticsService.saveTotalMember(tenantId,1);
    }
}

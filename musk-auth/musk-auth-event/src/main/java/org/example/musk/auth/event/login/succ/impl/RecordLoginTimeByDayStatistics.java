package org.example.musk.auth.event.login.succ.impl;


import jakarta.annotation.Resource;
import org.example.musk.auth.event.login.succ.LoginSuccEventIntl;
import org.example.musk.auth.event.login.succ.entity.LoginSuccEventInfo;
import org.example.musk.auth.service.core.statistics.logintimebydaystatistics.LoginTimeByDayStatisticsService;
import org.springframework.stereotype.Service;

@Service
public class RecordLoginTimeByDayStatistics implements LoginSuccEventIntl {


    @Resource
    private LoginTimeByDayStatisticsService loginTimeByDayStatisticsService;

    @Override
    public void event(LoginSuccEventInfo loginSuccEventInfo) {
        loginTimeByDayStatisticsService.saveLoginTimeByDayStatistics(loginSuccEventInfo.getMemberDO().getId());
    }
}

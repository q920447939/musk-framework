package org.example.musk.auth.service.core.statistics.logintimebydaystatistics;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.dao.logintimebydaystatistics.LoginTimeByDayStatisticsMapper;
import org.example.musk.auth.entity.logintimebydaystatistics.LoginTimeByDayStatisticsDO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;


/**
 * 会员登录次数按天统计 Service 实现类
 *
 * @author 马斯克源码
 */
@Service
@Validated
@Slf4j
public class LoginTimeByDayStatisticsServiceImpl extends ServiceImpl<LoginTimeByDayStatisticsMapper, LoginTimeByDayStatisticsDO> implements LoginTimeByDayStatisticsService {

    @Resource
    private LoginTimeByDayStatisticsMapper loginTimeByDayStatisticsMapper;

    @Override
    public boolean saveLoginTimeByDayStatistics(Integer memberId ) {
        LocalDate today = LocalDate.now();
        LoginTimeByDayStatisticsDO db = loginTimeByDayStatisticsMapper.selectOne(
                new LambdaQueryWrapper<LoginTimeByDayStatisticsDO>()
                        .eq(LoginTimeByDayStatisticsDO::getMemberId, memberId)
                        .eq(LoginTimeByDayStatisticsDO::getLoginDate, today));
        if (null != db) {
            LoginTimeByDayStatisticsDO update = new LoginTimeByDayStatisticsDO();
            update.setCount(db.getCount() + 1);
            update.setId(db.getId());
            return  loginTimeByDayStatisticsMapper.updateById(update) > 0;
        }
        LoginTimeByDayStatisticsDO info = new LoginTimeByDayStatisticsDO();
        info.setMemberId(memberId);
        info.setLoginDate(today);
        info.setCount(1);
        loginTimeByDayStatisticsMapper.insert(info);
        return true;
    }

    @Override
    public List<LoginTimeByDayStatisticsDO> queryLoginTimeByDayStatistics(List<Integer> memberIdList, LocalDate date) {
        return this.baseMapper.selectList( new LambdaQueryWrapper<LoginTimeByDayStatisticsDO>()
                .in(LoginTimeByDayStatisticsDO::getMemberId, memberIdList)
                .eq(LoginTimeByDayStatisticsDO::getLoginDate, date));
    }
}

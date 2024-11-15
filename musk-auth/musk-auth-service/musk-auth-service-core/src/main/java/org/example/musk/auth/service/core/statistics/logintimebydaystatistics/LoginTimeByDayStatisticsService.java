package org.example.musk.auth.service.core.statistics.logintimebydaystatistics;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;
import org.example.musk.auth.entity.logintimebydaystatistics.LoginTimeByDayStatisticsDO;

import java.time.LocalDate;
import java.util.List;


/**
 * 会员登录次数按天统计 Service 接口
 *
 * @author 马斯克源码
 */
public interface LoginTimeByDayStatisticsService extends IService<LoginTimeByDayStatisticsDO> {

    /**
     * 保存会员登录次数按天统计
     *
     * @param memberId 会员
     * @return 编号
     */
    boolean saveLoginTimeByDayStatistics(@Valid Integer memberId );


    List<LoginTimeByDayStatisticsDO> queryLoginTimeByDayStatistics(List<Integer> memberIdList, LocalDate date);
}

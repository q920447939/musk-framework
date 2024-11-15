package org.example.musk.auth.service.core.statistics.tenantregisterdailystatistics;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.auth.entity.tenantregisterdailystatistics.TenantRegisterDailyStatisticsDO;

import java.time.LocalDate;


/**
 * 租户注册按日期统计 Service 接口
 *
 * @author 马斯克源码
 */
public interface TenantRegisterDailyStatisticsService extends IService<TenantRegisterDailyStatisticsDO> {


    Long saveTenantRegisterDailyStatistics(Integer tenantId, LocalDate registerDate);

}

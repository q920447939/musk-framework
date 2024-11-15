package org.example.musk.auth.service.core.statistics.tenantregisterdailystatistics;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.constants.cache.RedisCacheTenantDateConstant;
import org.example.musk.auth.constants.lock.RedisLockConstant;
import org.example.musk.auth.dao.tenantregisterdailystatistics.TenantRegisterDailyStatisticsMapper;
import org.example.musk.auth.entity.tenantregisterdailystatistics.TenantRegisterDailyStatisticsDO;
import org.example.musk.auth.service.core.wrapper.lock.LockWrapper;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;


/**
 * 租户注册按日期统计 Service 实现类
 *
 * @author 马斯克源码
 */
@Service
@Validated
@Slf4j
public class TenantRegisterDailyStatisticsServiceImpl extends ServiceImpl<TenantRegisterDailyStatisticsMapper, TenantRegisterDailyStatisticsDO> implements TenantRegisterDailyStatisticsService {

    @Resource
    private TenantRegisterDailyStatisticsMapper tenantRegisterDailyStatisticsMapper;

    @Resource
    private RedisUtil redisUtil;

    private static final int TTL = 60 * 60 * 24;

    @Override
    public Long saveTenantRegisterDailyStatistics(Integer tenantId, LocalDate registerDate) {
        String formatDate = LocalDateTimeUtil.formatNormal(registerDate);

        String lockKey = String.format(RedisLockConstant.TENANT_CONTEXT_REDIS_REGISTER_DAILY_STATISTICS,tenantId,  formatDate);
        return LockWrapper.facadeLock(lockKey, () -> {
            String cacheKey = String.format(RedisCacheTenantDateConstant.TENANT_CONTEXT_REDIS_CACHE_TYPE_REGISTER_DAILY_STATISTICS,tenantId,  formatDate);
            Object o = redisUtil.get(cacheKey);
            if (o instanceof TenantRegisterDailyStatisticsDO dailyStatistics) {
                TenantRegisterDailyStatisticsDO update = new TenantRegisterDailyStatisticsDO();
                update.setNum(dailyStatistics.getNum());
                update.setId(dailyStatistics.getId());

                this.baseMapper.updateById(update);

                redisUtil.set(cacheKey, update, TTL);
                return update.getId();
            }
            TenantRegisterDailyStatisticsDO info = new TenantRegisterDailyStatisticsDO();
            info.setRegisterDate(registerDate);
            info.setNum(1);
            this.baseMapper.insert(info);

            redisUtil.set(cacheKey, info, TTL);
            return info.getId();
        });
    }
}

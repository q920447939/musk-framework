package org.example.musk.auth.service.core.statistics.tenanttotalstatistics;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.constants.lock.RedisLockConstant;
import org.example.musk.auth.dao.tenanttotalstatistics.TenantTotalStatisticsMapper;
import org.example.musk.auth.entity.tenanttotalstatistics.TenantTotalStatisticsDO;
import org.example.musk.auth.service.core.wrapper.lock.LockWrapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 * 租户统计 Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
public class TenantTotalStatisticsServiceImpl extends ServiceImpl<TenantTotalStatisticsMapper, TenantTotalStatisticsDO> implements TenantTotalStatisticsService {

    @Resource
    private TenantTotalStatisticsMapper tenantTotalStatisticsMapper;

    @Override
    public boolean saveTotalMember(Integer tenantId, int num) {
        String lockKey = String.format(RedisLockConstant.TENANT_CONTEXT_REDIS_REGISTER_TOTAL_STATISTICS, tenantId);
        return LockWrapper.facadeLock(lockKey, () -> {
            if (!tenantTotalStatisticsMapper.exists(new LambdaQueryWrapper<TenantTotalStatisticsDO>())) {
                TenantTotalStatisticsDO info = new TenantTotalStatisticsDO();
                info.setTotalMember(1);
                info.setTenantId(Long.valueOf(tenantId));
                tenantTotalStatisticsMapper.insert(info);
            }
            TenantTotalStatisticsDO tenantTotalStatistics = getTenantTotalStatistics();

            if (null == tenantTotalStatistics) {
                return false;
            }
            TenantTotalStatisticsDO update = new TenantTotalStatisticsDO();
            update.setTotalMember(tenantTotalStatistics.getTotalMember() + num);

            update.setId(tenantTotalStatistics.getId());
            return tenantTotalStatisticsMapper.updateById(update) > 0;

        });
    }

    public TenantTotalStatisticsDO getTenantTotalStatistics() {
        return this.baseMapper.selectOne(new LambdaQueryWrapper<TenantTotalStatisticsDO>());
    }


}

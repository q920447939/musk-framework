package org.example.musk.auth.service.core.statistics.tenanttotalstatistics;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.auth.entity.tenanttotalstatistics.TenantTotalStatisticsDO;


/**
 * 租户统计 Service 接口
 *
 * @author 马斯克源码
 */
public interface TenantTotalStatisticsService extends IService<TenantTotalStatisticsDO> {

    /**
     * 更新 累计注册会员数
     *
     * @return 编号
     */
    boolean saveTotalMember(Integer tenantId, int num);


}

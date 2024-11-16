package org.example.musk.framework.tenant.service.tenant;


import org.example.musk.common.pojo.tenant.TenantDO;

import java.util.List;

/**
 * 租户 Service 接口
 *
 * @author
 */
public interface TenantService {


    /**
     * 获得租户
     *
     * @param id 编号
     * @return 租户
     */
    TenantDO getTenant(Long id);
    List<TenantDO> getEnableTenant();


}

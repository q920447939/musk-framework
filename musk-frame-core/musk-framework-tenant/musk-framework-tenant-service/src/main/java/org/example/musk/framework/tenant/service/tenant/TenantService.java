package org.example.musk.framework.tenant.service.tenant;


import jakarta.validation.Valid;
import org.example.musk.common.pojo.tenant.TenantDO;
import org.example.musk.framework.tenant.service.tenant.bo.TenantSaveReqBO;

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

    /**
     * 获得名字对应的租户
     *
     * @param name 租户名
     * @return 租户
     */
    TenantDO getTenantByName(String name);

    /**
     * 创建租户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTenant(@Valid TenantSaveReqBO createReqVO);


}

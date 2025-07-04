package org.example.musk.framework.tenant.service.tenant;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.musk.common.pojo.tenant.TenantPackageDO;

import java.util.List;

/**
 * 租户套餐 Service 接口
 *
 * @author
 */
public interface TenantPackageService extends IService<TenantPackageDO> {

    TenantPackageDO queryTenantPackageByPackageId(Long id);

    List<TenantPackageDO> queryEnableTenantPackages();

    /**
     * 校验租户套餐
     *
     * @param id 编号
     * @return 租户套餐
     */
    TenantPackageDO validTenantPackage(Long id);
}

package org.example.musk.framework.tenant.service.tenant;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.enums.tenant.CommonStatusEnum;
import org.example.musk.common.pojo.tenant.TenantPackageDO;
import org.example.musk.framework.tenant.dao.tenant.TenantPackageMapper;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static org.example.musk.common.exception.enums.GlobalErrorCodeConstants.TENANT_PACKAGE_DISABLE;
import static org.example.musk.common.exception.enums.GlobalErrorCodeConstants.TENANT_PACKAGE_NOT_EXISTS;
import static org.example.musk.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @Description:
 * @date 2024年07月05日
 */
@Service
@Validated
@Slf4j
public class TenantPackageServiceImpl extends ServiceImpl<TenantPackageMapper, TenantPackageDO> implements TenantPackageService {

    @Override
    public TenantPackageDO queryTenantPackageByPackageId(Long id) {
        return this.baseMapper.selectOne(new LambdaQueryWrapperX<TenantPackageDO>()
                .eq(TenantPackageDO::getId,id)
                .eq(TenantPackageDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
        );
    }

    @Override
    public List<TenantPackageDO> queryEnableTenantPackages() {
        return this.baseMapper.selectList(new LambdaQueryWrapperX<TenantPackageDO>()
                .eq(TenantPackageDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
        );
    }

    @Override
    public TenantPackageDO validTenantPackage(Long id) {
        TenantPackageDO tenantPackage = baseMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        if (tenantPackage.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_PACKAGE_DISABLE, tenantPackage.getName());
        }
        return tenantPackage;
    }

}

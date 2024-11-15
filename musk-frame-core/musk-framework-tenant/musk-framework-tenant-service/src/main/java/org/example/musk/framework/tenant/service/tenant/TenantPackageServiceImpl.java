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
}

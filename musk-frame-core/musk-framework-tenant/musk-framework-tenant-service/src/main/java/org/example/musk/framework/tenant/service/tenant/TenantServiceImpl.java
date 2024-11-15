package org.example.musk.framework.tenant.service.tenant;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.enums.tenant.CommonStatusEnum;
import org.example.musk.common.pojo.tenant.TenantDO;
import org.example.musk.framework.tenant.dao.tenant.TenantMapper;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * 租户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class TenantServiceImpl implements TenantService {
    @Resource
    private TenantMapper tenantMapper;

    @Override
    public TenantDO getTenant(Long id) {
        TenantDO tenantDO = tenantMapper.selectById(id);
        if (null == tenantDO) {
            return null;
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(tenantDO.getStatus())) {
            return null;
        }
        return tenantDO;
    }

    @Override
    public List<TenantDO> getEnableTenant() {
        return tenantMapper.selectList(new LambdaQueryWrapperX<TenantDO>().eq(TenantDO::getStatus,CommonStatusEnum.ENABLE.getStatus()));
    }
}

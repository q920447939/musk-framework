package org.example.musk.framework.tenant.service.tenant;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.enums.tenant.CommonStatusEnum;
import org.example.musk.common.pojo.tenant.TenantDO;
import org.example.musk.common.pojo.tenant.TenantPackageDO;
import org.example.musk.common.util.object.BeanUtils;
import org.example.musk.framework.tenant.dao.tenant.TenantMapper;
import org.example.musk.framework.tenant.service.tenant.bo.TenantSaveReqBO;
import org.example.musk.framework.tenant.service.utils.TenantUtils;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static org.example.musk.common.exception.enums.GlobalErrorCodeConstants.TENANT_NAME_DUPLICATE;
import static org.example.musk.common.exception.enums.GlobalErrorCodeConstants.TENANT_WEBSITE_DUPLICATE;
import static org.example.musk.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 租户 Service 实现类
 *
 * @author
 */
@Service
@Validated
@Slf4j
public class TenantServiceImpl implements TenantService {
    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private TenantPackageService tenantPackageService;
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

    @Override
    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectByName(name);
    }

    @Override
    @DSTransactional // 多数据源，使用 @DSTransactional 保证本地事务，以及数据源的切换
    public Long createTenant(TenantSaveReqBO createReqVO) {
        // 校验租户名称是否重复
        validTenantNameDuplicate(createReqVO.getName(), null);
        // 校验租户域名是否重复
        validTenantWebsiteDuplicate(createReqVO.getWebsite(), null);
        // 校验套餐被禁用
        TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(createReqVO.getPackageId());

        // 创建租户
        TenantDO tenant = BeanUtils.toBean(createReqVO, TenantDO.class);
        tenantMapper.insert(tenant);
        // 创建租户的管理员
        //TODO
       /* TenantUtils.execute(tenant.getId(), () -> {
            // 创建角色
            Long roleId = createRole(tenantPackage);
            // 创建用户，并分配角色
            Long userId = createUser(roleId, createReqVO);
            // 修改租户的管理员
            tenantMapper.updateById(new TenantDO().setId(tenant.getId()).setContactUserId(userId));
        });*/
        return tenant.getId();
    }

    private void validTenantNameDuplicate(String name, Long id) {
        TenantDO tenant = tenantMapper.selectByName(name);
        if (tenant == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同名字的租户
        if (id == null) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
        if (!tenant.getId().equals(id)) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
    }

    private void validTenantWebsiteDuplicate(String website, Long id) {
        if (StrUtil.isEmpty(website)) {
            return;
        }
        TenantDO tenant = tenantMapper.selectByWebsite(website);
        if (tenant == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同名字的租户
        if (id == null) {
            throw exception(TENANT_WEBSITE_DUPLICATE, website);
        }
        if (!tenant.getId().equals(id)) {
            throw exception(TENANT_WEBSITE_DUPLICATE, website);
        }
    }

}

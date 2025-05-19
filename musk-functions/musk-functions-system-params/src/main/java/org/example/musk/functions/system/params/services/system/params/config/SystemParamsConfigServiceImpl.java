package org.example.musk.functions.system.params.services.system.params.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.paramsconfig.SystemParamsConfigDO;
import org.example.musk.constant.cache.RedisCacheTenantConstant;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.enums.appConfig.AppParamConfigEnums;
import org.example.musk.enums.appConfig.AppParamsConfigGroupEnums;
import org.example.musk.enums.appConfig.SystemDomain;
import org.example.musk.enums.appConfig.AppParamsConfigTypeEnums;
import org.example.musk.framework.tenant.config.TenantConfig;
import org.example.musk.functions.system.params.mapper.system.params.config.SystemParamsConfigMapper;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * 参数配置 Service 实现类
 *
 * @author 马斯克源码
 */
@Service
@Validated
@DS(DBConstant.SYSTEM)
@Slf4j
public class SystemParamsConfigServiceImpl extends ServiceImpl<SystemParamsConfigMapper, SystemParamsConfigDO> implements SystemParamsConfigService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TenantConfig tenantConfig;

    @Override
    public List<SystemParamsConfigDO> queryAppParamsConfigByGroup(SystemDomain systemDomain, AppParamsConfigGroupEnums appParamsConfigGroupEnums) {
        return this.baseMapper.selectList(new LambdaQueryWrapperX<SystemParamsConfigDO>()
                .eq(SystemParamsConfigDO::getDomainId, systemDomain.getDomain())
                .eq(SystemParamsConfigDO::getStatus, AppParamConfigEnums.VALID.getStatus())
        );
    }


    @Override
    public SystemParamsConfigDO queryAppParamsConfigByType(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums) {
        List<SystemParamsConfigDO> systemParamsConfigDOList = queryAppParamsConfigByTypes(tenantId, systemDomain,appParamsConfigTypeEnums,true);

        return CollUtil.isEmpty(systemParamsConfigDOList) ? null : systemParamsConfigDOList.getFirst();
    }

    public List<SystemParamsConfigDO> queryAppParamsConfigByTypes(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums, boolean useCache) {
        if (!useCache) {
            return queryAppParamsConfig(tenantId, systemDomain,appParamsConfigTypeEnums);
        }
        String key = String.format(RedisCacheTenantConstant.TENANT_CONTEXT_REDIS_CACHE_SYSTEM_APP_PARAMS, tenantId,
                (systemDomain.getDomain() + "_" + appParamsConfigTypeEnums.getAppParamsConfigGroupEnum().getGroup()+ "_" + appParamsConfigTypeEnums.getType()));
        List<SystemParamsConfigDO> list = redisUtil.lGetAll(key);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        List<SystemParamsConfigDO> systemParamsConfigDOList = queryAppParamsConfig(tenantId, systemDomain, appParamsConfigTypeEnums);
        redisUtil.lSetDefaultTtl(key, systemParamsConfigDOList);
        return systemParamsConfigDOList;
    }



    private List<SystemParamsConfigDO> queryAppParamsConfig(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums) {
        List<SystemParamsConfigDO> appParamsConfigList = this.baseMapper.selectList(new LambdaQueryWrapperX<SystemParamsConfigDO>()
                .eq(SystemParamsConfigDO::getTenantId, tenantId)
                .eq(SystemParamsConfigDO::getDomainId, systemDomain.getDomain())
                .eq(SystemParamsConfigDO::getType, appParamsConfigTypeEnums.getType())
                .eq(SystemParamsConfigDO::getStatus, AppParamConfigEnums.VALID.getStatus())
        );
        if (CollUtil.isNotEmpty(appParamsConfigList)) {
            return appParamsConfigList;
        }
        //未查到配置，用租户1 的配置
        return this.baseMapper.selectList(new LambdaQueryWrapperX<SystemParamsConfigDO>()
                .eq(SystemParamsConfigDO::getTenantId, tenantConfig.getConfigDefaultTenantId())
                .eq(SystemParamsConfigDO::getDomainId, systemDomain.getDomain())
                .eq(SystemParamsConfigDO::getType, appParamsConfigTypeEnums.getType())
                .eq(SystemParamsConfigDO::getStatus, AppParamConfigEnums.VALID.getStatus())
        );
    }

    @Override
    public List<SystemParamsConfigDO> queryAppParamsConfigByTypes(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums) {
        return queryAppParamsConfigByTypes(tenantId, systemDomain,appParamsConfigTypeEnums,true);
    }
}

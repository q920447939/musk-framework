package org.example.musk.functions.system.params.services.paramsconfig;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.paramsconfig.AppParamsConfigDO;
import org.example.musk.constant.cache.RedisCacheTenantConstant;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.enums.appConfig.AppParamConfigEnums;
import org.example.musk.enums.appConfig.AppParamsConfigGroupEnums;
import org.example.musk.enums.appConfig.AppParamsConfigSystemEnums;
import org.example.musk.enums.appConfig.AppParamsConfigTypeEnums;
import org.example.musk.framework.tenant.config.TenantConfig;
import org.example.musk.functions.system.params.mapper.paramsconfig.AppParamsConfigMapper;
import org.example.musk.middleware.mybatisplus.mybatis.core.query.LambdaQueryWrapperX;
import org.example.musk.middleware.redis.RedisUtil;
import org.springframework.context.annotation.Lazy;
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
@DS(DBConstant.APP_CONFIG)
@Slf4j
public class AppParamsConfigServiceImpl extends ServiceImpl<AppParamsConfigMapper, AppParamsConfigDO> implements AppParamsConfigService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TenantConfig tenantConfig;

    @Override
    public List<AppParamsConfigDO> queryAppParamsConfigByGroup(AppParamsConfigSystemEnums appParamsConfigSystemEnums,AppParamsConfigGroupEnums appParamsConfigGroupEnums) {
        return this.baseMapper.selectList(new LambdaQueryWrapperX<AppParamsConfigDO>()
                .eq(AppParamsConfigDO::getBSystem, appParamsConfigSystemEnums.getSystem())
                .eq(AppParamsConfigDO::getBGroup, appParamsConfigGroupEnums.getGroup())
                .eq(AppParamsConfigDO::getStatus, AppParamConfigEnums.VALID.getStatus())
        );
    }


    @Override
    public AppParamsConfigDO queryAppParamsConfigByType(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums, AppParamsConfigTypeEnums appParamsConfigTypeEnums) {
        List<AppParamsConfigDO> appParamsConfigDOList = queryAppParamsConfigByTypes(tenantId,appParamsConfigSystemEnums ,appParamsConfigTypeEnums,true);

        return CollUtil.isEmpty(appParamsConfigDOList) ? null : appParamsConfigDOList.getFirst();
    }

    public List<AppParamsConfigDO> queryAppParamsConfigByTypes(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums, AppParamsConfigTypeEnums appParamsConfigTypeEnums,boolean useCache) {
        if (!useCache) {
            return queryAppParamsConfig(tenantId,appParamsConfigSystemEnums,appParamsConfigTypeEnums);
        }
        String key = String.format(RedisCacheTenantConstant.TENANT_CONTEXT_REDIS_CACHE_SYSTEM_APP_PARAMS, tenantId,
                (appParamsConfigSystemEnums.getSystem() + "_" + appParamsConfigTypeEnums.getAppParamsConfigGroupEnum().getGroup()+ "_" + appParamsConfigTypeEnums.getType()));
        List<AppParamsConfigDO> list = redisUtil.lGetAll(key);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        List<AppParamsConfigDO> appParamsConfigDOList = queryAppParamsConfig(tenantId,appParamsConfigSystemEnums, appParamsConfigTypeEnums);
        redisUtil.lSetDefaultTtl(key,appParamsConfigDOList);
        return appParamsConfigDOList;
    }



    private List<AppParamsConfigDO> queryAppParamsConfig(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums, AppParamsConfigTypeEnums appParamsConfigTypeEnums) {
        List<AppParamsConfigDO> appParamsConfigList = this.baseMapper.selectList(new LambdaQueryWrapperX<AppParamsConfigDO>()
                .eq(AppParamsConfigDO::getTenantId, tenantId)
                .eq(AppParamsConfigDO::getBSystem, appParamsConfigSystemEnums.getSystem())
                .eq(AppParamsConfigDO::getBGroup, appParamsConfigTypeEnums.getAppParamsConfigGroupEnum().getGroup())
                .eq(AppParamsConfigDO::getType, appParamsConfigTypeEnums.getType())
                .eq(AppParamsConfigDO::getStatus, AppParamConfigEnums.VALID.getStatus())
        );
        if (CollUtil.isNotEmpty(appParamsConfigList)) {
            return appParamsConfigList;
        }
        //未查到配置，用租户1 的配置
        return this.baseMapper.selectList(new LambdaQueryWrapperX<AppParamsConfigDO>()
                .eq(AppParamsConfigDO::getTenantId, tenantConfig.getConfigDefaultTenantId())
                .eq(AppParamsConfigDO::getBSystem, appParamsConfigSystemEnums.getSystem())
                .eq(AppParamsConfigDO::getBGroup, appParamsConfigTypeEnums.getAppParamsConfigGroupEnum().getGroup())
                .eq(AppParamsConfigDO::getType, appParamsConfigTypeEnums.getType())
                .eq(AppParamsConfigDO::getStatus, AppParamConfigEnums.VALID.getStatus())
        );
    }

    @Override
    public List<AppParamsConfigDO> queryAppParamsConfigByTypes(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums,AppParamsConfigTypeEnums appParamsConfigTypeEnums) {
        return queryAppParamsConfigByTypes(tenantId,appParamsConfigSystemEnums,appParamsConfigTypeEnums,true);
    }
}

package org.example.musk.functions.system.params.services.system.params.config;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.paramsconfig.SystemParamsConfigDO;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.enums.appConfig.AppParamsConfigGroupEnums;
import org.example.musk.enums.appConfig.SystemDomain;
import org.example.musk.enums.appConfig.AppParamsConfigTypeEnums;

import java.math.BigDecimal;
import java.util.List;


/**
 * 参数配置 Service 接口
 *
 * @author 马斯克源码
 */
public interface SystemParamsConfigService extends IService<SystemParamsConfigDO> {

    List<SystemParamsConfigDO> queryAppParamsConfigByGroup(SystemDomain systemDomain, AppParamsConfigGroupEnums appParamsConfigGroupEnums);
    SystemParamsConfigDO queryAppParamsConfigByType(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums);

    List<SystemParamsConfigDO> queryAppParamsConfigByTypes(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums);

    default String querySystemParamsConfigByTypeValue1(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        SystemParamsConfigDO systemParamsConfigDO = this.queryAppParamsConfigByType(tenantId, systemDomain, appParamsConfigTypeEnums);
        if (null == systemParamsConfigDO) {
            org.example.musk.common.exception.BusinessPageExceptionEnum  anEnum = org.example.musk.common.exception.BusinessPageExceptionEnum.APP_PARAM_CONFIG_IS_EMPTY;
            throw new BusinessException(anEnum);
        }
        return systemParamsConfigDO.getValue1();
    }

    default int queryAppParamsConfigByTypeValue1ToInt(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        String value1 = this.querySystemParamsConfigByTypeValue1(tenantId, systemDomain,appParamsConfigTypeEnums);
        return Integer.parseInt(value1);
    }
    default BigDecimal queryAppParamsConfigByTypeValue1ToBigDecimal(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        String value1 = this.querySystemParamsConfigByTypeValue1(tenantId, systemDomain,appParamsConfigTypeEnums);
        return BigDecimal.valueOf(Double.parseDouble(value1));
    }
    default Boolean queryAppParamsConfigByTypeValue1ToBoolean(Integer tenantId, SystemDomain systemDomain, AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        String value1 = this.querySystemParamsConfigByTypeValue1(tenantId, systemDomain,appParamsConfigTypeEnums);
        return Boolean.valueOf(value1);
    }


}

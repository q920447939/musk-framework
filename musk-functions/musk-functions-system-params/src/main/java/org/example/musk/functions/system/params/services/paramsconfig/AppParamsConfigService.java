package org.example.musk.functions.system.params.services.paramsconfig;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.paramsconfig.AppParamsConfigDO;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.enums.appConfig.AppParamsConfigGroupEnums;
import org.example.musk.enums.appConfig.AppParamsConfigSystemEnums;
import org.example.musk.enums.appConfig.AppParamsConfigTypeEnums;

import java.math.BigDecimal;
import java.util.List;


/**
 * 参数配置 Service 接口
 *
 * @author 马斯克源码
 */
public interface AppParamsConfigService extends IService<AppParamsConfigDO> {

    List<AppParamsConfigDO> queryAppParamsConfigByGroup(AppParamsConfigSystemEnums appParamsConfigSystemEnums, AppParamsConfigGroupEnums appParamsConfigGroupEnums);
    AppParamsConfigDO queryAppParamsConfigByType(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums,AppParamsConfigTypeEnums appParamsConfigTypeEnums);

    List<AppParamsConfigDO> queryAppParamsConfigByTypes(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums, AppParamsConfigTypeEnums appParamsConfigTypeEnums);

    default String queryAppParamsConfigByTypeValue1(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums, AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        AppParamsConfigDO appParamsConfigDO = this.queryAppParamsConfigByType(tenantId,appParamsConfigSystemEnums, appParamsConfigTypeEnums);
        if (null == appParamsConfigDO) {
            org.example.musk.common.exception.BusinessPageExceptionEnum  anEnum = org.example.musk.common.exception.BusinessPageExceptionEnum.APP_PARAM_CONFIG_IS_EMPTY;
            throw new BusinessException(anEnum);
        }
        return appParamsConfigDO.getValue1();
    }

    default int queryAppParamsConfigByTypeValue1ToInt(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums, AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        String value1 = this.queryAppParamsConfigByTypeValue1(tenantId,appParamsConfigSystemEnums,appParamsConfigTypeEnums);
        return Integer.parseInt(value1);
    }
    default BigDecimal queryAppParamsConfigByTypeValue1ToBigDecimal(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums,AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        String value1 = this.queryAppParamsConfigByTypeValue1(tenantId,appParamsConfigSystemEnums,appParamsConfigTypeEnums);
        return BigDecimal.valueOf(Double.parseDouble(value1));
    }
    default Boolean queryAppParamsConfigByTypeValue1ToBoolean(Integer tenantId,AppParamsConfigSystemEnums appParamsConfigSystemEnums,AppParamsConfigTypeEnums appParamsConfigTypeEnums){
        String value1 = this.queryAppParamsConfigByTypeValue1(tenantId,appParamsConfigSystemEnums,appParamsConfigTypeEnums);
        return Boolean.valueOf(value1);
    }


}

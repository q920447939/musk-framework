package org.example.musk.functions.resource.storage;

import org.example.musk.functions.resource.enums.StorageTypeEnum;
import org.example.musk.functions.resource.exception.ResourceException;
import org.example.musk.functions.resource.storage.impl.LocalStorageStrategy;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 存储策略工厂
 *
 * @author musk-functions-resource
 */
@Component
public class StorageStrategyFactory {

    /**
     * 存储策略映射
     */
    private final Map<Integer, ResourceStorageStrategy> strategyMap = new HashMap<>();

    /**
     * 本地存储策略
     */
    @Resource
    private LocalStorageStrategy localStorageStrategy;

    /**
     * 获取存储策略
     *
     * @param storageType 存储类型
     * @return 存储策略
     */
    public ResourceStorageStrategy getStrategy(Integer storageType) {
        // 延迟初始化
        if (strategyMap.isEmpty()) {
            initStrategies();
        }

        ResourceStorageStrategy strategy = strategyMap.get(storageType);
        if (strategy == null) {
            throw new ResourceException("不支持的存储类型: " + storageType);
        }
        return strategy;
    }

    /**
     * 初始化存储策略
     */
    private void initStrategies() {
        // 注册本地存储策略
        strategyMap.put(StorageTypeEnum.LOCAL.getCode(), localStorageStrategy);

        // 可以在这里注册其他存储策略
        // strategyMap.put(StorageTypeEnum.ALIYUN_OSS.getCode(), aliyunOssStorageStrategy);
        // strategyMap.put(StorageTypeEnum.TENCENT_COS.getCode(), tencentCosStorageStrategy);
    }
}

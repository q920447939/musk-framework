package org.example.musk.functions.resource.storage;

import java.io.InputStream;

/**
 * 资源存储策略接口
 *
 * @author musk-functions-resource
 */
public interface ResourceStorageStrategy {

    /**
     * 存储资源
     *
     * @param inputStream 资源输入流
     * @param fileName    文件名
     * @param fileSize    文件大小
     * @param fileType    文件类型
     * @return 存储结果
     */
    StorageResult store(InputStream inputStream, String fileName, long fileSize, String fileType);

    /**
     * 删除资源
     *
     * @param storagePath 存储路径
     * @return 是否删除成功
     */
    boolean delete(String storagePath);

    /**
     * 获取资源访问URL
     *
     * @param storagePath   存储路径
     * @param expireSeconds 过期时间（秒）
     * @return 访问URL
     */
    String getAccessUrl(String storagePath, int expireSeconds);

    /**
     * 获取资源内容
     *
     * @param storagePath 存储路径
     * @return 资源输入流
     */
    InputStream getContent(String storagePath);

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    Integer getStorageType();
}

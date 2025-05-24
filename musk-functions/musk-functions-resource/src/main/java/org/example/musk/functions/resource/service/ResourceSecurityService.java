package org.example.musk.functions.resource.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 资源安全服务接口
 *
 * @author musk-functions-resource
 */
public interface ResourceSecurityService {

    /**
     * 检查文件类型
     *
     * @param file 文件
     */
    void checkFileType(MultipartFile file);

    /**
     * 检查文件大小
     *
     * @param file 文件
     */
    void checkFileSize(MultipartFile file);

    /**
     * 验证URL签名
     *
     * @param url       URL
     * @param signature 签名
     * @param timestamp 时间戳
     * @return 是否有效
     */
    boolean validateSignature(String url, String signature, long timestamp);

    /**
     * 生成URL签名
     *
     * @param url       URL
     * @param timestamp 时间戳
     * @return 签名
     */
    String generateSignature(String url, long timestamp);

    /**
     * 检查Referer
     *
     * @param referer Referer
     * @return 是否允许
     */
    boolean checkReferer(String referer);

    /**
     * 检查存储空间
     *
     * @param fileSize 文件大小
     * @return 是否允许
     */
    boolean checkStorageSpace(long fileSize);

    /**
     * 检查文件数量
     *
     * @return 是否允许
     */
    boolean checkFileCount();
}

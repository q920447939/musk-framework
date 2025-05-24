package org.example.musk.functions.resource.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储结果
 *
 * @author musk-functions-resource
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageResult {

    /**
     * 存储路径
     */
    private String storagePath;

    /**
     * 访问URL
     */
    private String accessUrl;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 存储类型
     */
    private Integer storageType;

    /**
     * 缩略图URL（如果有）
     */
    private String thumbnailUrl;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;
}

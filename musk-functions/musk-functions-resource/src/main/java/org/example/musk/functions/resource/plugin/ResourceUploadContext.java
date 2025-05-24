package org.example.musk.functions.resource.plugin;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 资源上传上下文
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceUploadContext {

    /**
     * 原始文件
     */
    private MultipartFile file;

    /**
     * 文件输入流
     */
    private InputStream inputStream;

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
     * 资源类型
     */
    private Integer resourceType;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 标签
     */
    private String tags;

    /**
     * 描述
     */
    private String description;

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 域ID
     */
    private Integer domainId;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 操作人类型
     */
    private Integer operatorType;

    /**
     * 是否跳过后续插件处理
     */
    private boolean skipRemaining = false;

    /**
     * 是否中止上传
     */
    private boolean abortUpload = false;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 跳过后续插件处理
     */
    public void skipRemaining() {
        this.skipRemaining = true;
    }

    /**
     * 中止上传
     *
     * @param errorMessage 错误信息
     */
    public void abortUpload(String errorMessage) {
        this.abortUpload = true;
        this.errorMessage = errorMessage;
    }
}

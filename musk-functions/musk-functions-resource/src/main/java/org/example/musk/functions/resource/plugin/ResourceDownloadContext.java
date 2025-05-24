package org.example.musk.functions.resource.plugin;

import lombok.Data;
import org.example.musk.functions.resource.entity.SystemResourceDO;

import java.io.InputStream;

/**
 * 资源下载上下文
 *
 * @author musk-functions-resource
 */
@Data
public class ResourceDownloadContext {

    /**
     * 资源信息
     */
    private SystemResourceDO resource;

    /**
     * 文件输入流
     */
    private InputStream inputStream;

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
     * 是否中止下载
     */
    private boolean abortDownload = false;

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
     * 中止下载
     *
     * @param errorMessage 错误信息
     */
    public void abortDownload(String errorMessage) {
        this.abortDownload = true;
        this.errorMessage = errorMessage;
    }
}

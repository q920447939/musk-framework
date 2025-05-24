package org.example.musk.functions.resource.plugin;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.enums.OperatorTypeEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 资源插件管理器
 *
 * @author musk-functions-resource
 */
@Component
@Slf4j
public class ResourcePluginManager {

    /**
     * 插件列表
     */
    private final List<ResourcePlugin> plugins = new ArrayList<>();

    /**
     * 初始化插件
     */
    @PostConstruct
    public void init() {
        // 加载插件
        ServiceLoader<ResourcePlugin> loader = ServiceLoader.load(ResourcePlugin.class);
        for (ResourcePlugin plugin : loader) {
            try {
                log.info("加载资源插件: {}", plugin.getName());
                plugin.init();
                plugins.add(plugin);
            } catch (Exception e) {
                log.error("初始化资源插件失败: {}", plugin.getName(), e);
            }
        }

        // 按优先级排序
        plugins.sort(Comparator.comparingInt(ResourcePlugin::getOrder));
    }

    /**
     * 执行上传前处理
     *
     * @param file        文件
     * @param categoryId  分类ID
     * @param tags        标签
     * @param description 描述
     * @return 上传上下文
     */
    public ResourceUploadContext executeBeforeUpload(MultipartFile file, Integer categoryId, String tags, String description) {
        // 创建上下文
        ResourceUploadContext context = new ResourceUploadContext();
        context.setFile(file);
        try {
            context.setInputStream(file.getInputStream());
        } catch (Exception e) {
            log.error("获取文件输入流失败", e);
            context.abortUpload("获取文件输入流失败: " + e.getMessage());
            return context;
        }
        context.setFileName(file.getOriginalFilename());
        context.setFileSize(file.getSize());
        context.setFileType(getFileExtension(file.getOriginalFilename()));
        context.setCategoryId(categoryId);
        context.setTags(tags);
        context.setDescription(description);
        context.setTenantId(ThreadLocalTenantContext.getTenantId());
        context.setDomainId(ThreadLocalTenantContext.getDomainId());

        // 获取操作人信息
        Integer operatorId = ThreadLocalTenantContext.getMemberId();
        Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
        if (operatorId == null) {
            operatorId = 0; // 临时使用0表示系统用户
        }
        context.setOperatorId(operatorId);
        context.setOperatorType(operatorType);

        // 执行插件处理
        for (ResourcePlugin plugin : plugins) {
            try {
                if (context.isSkipRemaining() || context.isAbortUpload()) {
                    break;
                }
                plugin.beforeUpload(context);
            } catch (Exception e) {
                log.error("执行资源上传前处理插件失败: {}", plugin.getName(), e);
                context.abortUpload("执行资源上传前处理插件失败: " + e.getMessage());
                break;
            }
        }

        return context;
    }

    /**
     * 执行上传后处理
     *
     * @param resource 资源信息
     */
    public void executeAfterUpload(SystemResourceDO resource) {
        // 创建上下文
        ResourceUploadContext context = new ResourceUploadContext();
        context.setFileName(resource.getResourceName());
        context.setFileSize(resource.getFileSize());
        context.setFileType(resource.getFileType());
        context.setResourceType(resource.getResourceType());
        context.setCategoryId(resource.getCategoryId());
        context.setTags(resource.getTags());
        context.setDescription(resource.getDescription());
        context.setTenantId(resource.getTenantId());
        context.setDomainId(resource.getDomainId());

        // 获取操作人信息
        Integer operatorId = ThreadLocalTenantContext.getMemberId();
        Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
        if (operatorId == null) {
            operatorId = 0; // 临时使用0表示系统用户
        }
        context.setOperatorId(operatorId);
        context.setOperatorType(operatorType);

        // 执行插件处理
        for (ResourcePlugin plugin : plugins) {
            try {
                if (context.isSkipRemaining()) {
                    break;
                }
                plugin.afterUpload(context);
            } catch (Exception e) {
                log.error("执行资源上传后处理插件失败: {}", plugin.getName(), e);
            }
        }
    }

    /**
     * 执行下载前处理
     *
     * @param resource    资源信息
     * @param inputStream 文件输入流
     * @return 下载上下文
     */
    public ResourceDownloadContext executeBeforeDownload(SystemResourceDO resource, InputStream inputStream) {
        // 创建上下文
        ResourceDownloadContext context = new ResourceDownloadContext();
        context.setResource(resource);
        context.setInputStream(inputStream);
        context.setTenantId(resource.getTenantId());
        context.setDomainId(resource.getDomainId());

        // 获取操作人信息
        Integer operatorId = ThreadLocalTenantContext.getMemberId();
        Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
        if (operatorId == null) {
            operatorId = 0; // 临时使用0表示系统用户
        }
        context.setOperatorId(operatorId);
        context.setOperatorType(operatorType);

        // 执行插件处理
        for (ResourcePlugin plugin : plugins) {
            try {
                if (context.isSkipRemaining() || context.isAbortDownload()) {
                    break;
                }
                plugin.beforeDownload(context);
            } catch (Exception e) {
                log.error("执行资源下载前处理插件失败: {}", plugin.getName(), e);
                context.abortDownload("执行资源下载前处理插件失败: " + e.getMessage());
                break;
            }
        }

        return context;
    }

    /**
     * 执行下载后处理
     *
     * @param resource 资源信息
     */
    public void executeAfterDownload(SystemResourceDO resource) {
        // 创建上下文
        ResourceDownloadContext context = new ResourceDownloadContext();
        context.setResource(resource);
        context.setTenantId(resource.getTenantId());
        context.setDomainId(resource.getDomainId());

        // 获取操作人信息
        Integer operatorId = ThreadLocalTenantContext.getMemberId();
        Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
        if (operatorId == null) {
            operatorId = 0; // 临时使用0表示系统用户
        }
        context.setOperatorId(operatorId);
        context.setOperatorType(operatorType);

        // 执行插件处理
        for (ResourcePlugin plugin : plugins) {
            try {
                if (context.isSkipRemaining()) {
                    break;
                }
                plugin.afterDownload(context);
            } catch (Exception e) {
                log.error("执行资源下载后处理插件失败: {}", plugin.getName(), e);
            }
        }
    }

    /**
     * 执行删除前处理
     *
     * @param resource 资源信息
     * @return 删除上下文
     */
    public ResourceDeleteContext executeBeforeDelete(SystemResourceDO resource) {
        // 创建上下文
        ResourceDeleteContext context = new ResourceDeleteContext();
        context.setResource(resource);
        context.setTenantId(resource.getTenantId());
        context.setDomainId(resource.getDomainId());

        // 获取操作人信息
        Integer operatorId = ThreadLocalTenantContext.getMemberId();
        Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
        if (operatorId == null) {
            operatorId = 0; // 临时使用0表示系统用户
        }
        context.setOperatorId(operatorId);
        context.setOperatorType(operatorType);

        // 执行插件处理
        for (ResourcePlugin plugin : plugins) {
            try {
                if (context.isSkipRemaining() || context.isAbortDelete()) {
                    break;
                }
                plugin.beforeDelete(context);
            } catch (Exception e) {
                log.error("执行资源删除前处理插件失败: {}", plugin.getName(), e);
                context.abortDelete("执行资源删除前处理插件失败: " + e.getMessage());
                break;
            }
        }

        return context;
    }

    /**
     * 执行删除后处理
     *
     * @param resource 资源信息
     */
    public void executeAfterDelete(SystemResourceDO resource) {
        // 创建上下文
        ResourceDeleteContext context = new ResourceDeleteContext();
        context.setResource(resource);
        context.setTenantId(resource.getTenantId());
        context.setDomainId(resource.getDomainId());

        // 获取操作人信息
        Integer operatorId = ThreadLocalTenantContext.getMemberId();
        Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
        if (operatorId == null) {
            operatorId = 0; // 临时使用0表示系统用户
        }
        context.setOperatorId(operatorId);
        context.setOperatorType(operatorType);

        // 执行插件处理
        for (ResourcePlugin plugin : plugins) {
            try {
                if (context.isSkipRemaining()) {
                    break;
                }
                plugin.afterDelete(context);
            } catch (Exception e) {
                log.error("执行资源删除后处理插件失败: {}", plugin.getName(), e);
            }
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }
}

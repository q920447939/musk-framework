package org.example.musk.functions.resource.plugin;

/**
 * 资源插件接口
 *
 * @author musk-functions-resource
 */
public interface ResourcePlugin {

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String getName();

    /**
     * 插件初始化
     */
    void init();

    /**
     * 资源上传前处理
     *
     * @param context 上传上下文
     */
    void beforeUpload(ResourceUploadContext context);

    /**
     * 资源上传后处理
     *
     * @param context 上传上下文
     */
    void afterUpload(ResourceUploadContext context);

    /**
     * 资源下载前处理
     *
     * @param context 下载上下文
     */
    void beforeDownload(ResourceDownloadContext context);

    /**
     * 资源下载后处理
     *
     * @param context 下载上下文
     */
    void afterDownload(ResourceDownloadContext context);

    /**
     * 资源删除前处理
     *
     * @param context 删除上下文
     */
    void beforeDelete(ResourceDeleteContext context);

    /**
     * 资源删除后处理
     *
     * @param context 删除上下文
     */
    void afterDelete(ResourceDeleteContext context);

    /**
     * 获取插件优先级
     * 数值越小优先级越高
     *
     * @return 优先级
     */
    default int getOrder() {
        return 0;
    }
}

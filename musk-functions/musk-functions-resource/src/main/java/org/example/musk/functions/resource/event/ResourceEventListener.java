package org.example.musk.functions.resource.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 资源事件监听器
 *
 * @author musk-functions-resource
 */
@Component
@Slf4j
public class ResourceEventListener {

    /**
     * 处理资源上传事件
     *
     * @param event 资源上传事件
     */
    @EventListener
    public void handleResourceUploadedEvent(ResourceUploadedEvent event) {
        log.info("收到资源上传事件: resourceId={}, resourceName={}, resourceType={}, fileType={}, fileSize={}",
                event.getResourceId(), event.getResourceName(), event.getResourceType(), event.getFileType(), event.getFileSize());
        
        // 这里可以添加资源上传后的处理逻辑
        // 例如：发送通知、更新统计数据、触发后续处理等
    }

    /**
     * 处理资源删除事件
     *
     * @param event 资源删除事件
     */
    @EventListener
    public void handleResourceDeletedEvent(ResourceDeletedEvent event) {
        log.info("收到资源删除事件: resourceId={}, resourceName={}, resourceType={}, fileType={}",
                event.getResourceId(), event.getResourceName(), event.getResourceType(), event.getFileType());
        
        // 这里可以添加资源删除后的处理逻辑
        // 例如：清理相关数据、更新统计数据、触发后续处理等
    }
}

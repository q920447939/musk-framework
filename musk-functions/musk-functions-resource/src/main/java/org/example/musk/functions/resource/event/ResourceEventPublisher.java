package org.example.musk.functions.resource.event;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.functions.resource.entity.SystemResourceDO;
import org.example.musk.functions.resource.enums.OperatorTypeEnum;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 资源事件发布器
 *
 * @author musk-functions-resource
 */
@Component
@Slf4j
public class ResourceEventPublisher {

    @Resource
    private ApplicationEventPublisher eventPublisher;

    /**
     * 发布资源上传事件
     *
     * @param resource 资源信息
     */
    public void publishUploadEvent(SystemResourceDO resource) {
        try {
            // 获取操作人信息
            Integer operatorId = ThreadLocalTenantContext.getMemberId();
            Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
            if (operatorId == null) {
                operatorId = 0; // 临时使用0表示系统用户
            }

            // 创建事件
            ResourceUploadedEvent event = new ResourceUploadedEvent(
                    resource.getId(),
                    resource.getTenantId(),
                    resource.getDomainId(),
                    operatorId,
                    operatorType,
                    resource.getResourceName(),
                    resource.getResourceType(),
                    resource.getFileType(),
                    resource.getFileSize(),
                    resource.getStorageType(),
                    resource.getAccessUrl(),
                    resource.getCategoryId(),
                    resource.getTags()
            );

            // 发布事件
            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            // 事件发布失败不应影响主流程
            log.error("发布资源上传事件失败", e);
        }
    }

    /**
     * 发布资源删除事件
     *
     * @param resource 资源信息
     */
    public void publishDeleteEvent(SystemResourceDO resource) {
        try {
            // 获取操作人信息
            Integer operatorId = ThreadLocalTenantContext.getMemberId();
            Integer operatorType = operatorId != null ? OperatorTypeEnum.MEMBER.getCode() : OperatorTypeEnum.SYSTEM_USER.getCode();
            if (operatorId == null) {
                operatorId = 0; // 临时使用0表示系统用户
            }

            // 创建事件
            ResourceDeletedEvent event = new ResourceDeletedEvent(
                    resource.getId(),
                    resource.getTenantId(),
                    resource.getDomainId(),
                    operatorId,
                    operatorType,
                    resource.getResourceName(),
                    resource.getResourceType(),
                    resource.getFileType()
            );

            // 发布事件
            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            // 事件发布失败不应影响主流程
            log.error("发布资源删除事件失败", e);
        }
    }
}

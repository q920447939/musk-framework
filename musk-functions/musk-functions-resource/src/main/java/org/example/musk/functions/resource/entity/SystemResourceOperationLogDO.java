package org.example.musk.functions.resource.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;

import java.time.LocalDateTime;

/**
 * 资源操作日志表 DO
 *
 * @author musk-functions-resource
 */
@TableName("system_resource_operation_log")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceOperationLogDO extends DomainBaseDO {

    /**
     * 日志ID
     */
    @TableId
    private Integer id;

    /**
     * 资源ID
     */
    private Integer resourceId;

    /**
     * 操作类型（1:上传 2:下载 3:删除 4:修改 5:查看）
     */
    private Integer operationType;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 操作人类型（1:系统用户 2:会员）
     */
    private Integer operatorType;

    /**
     * 操作IP
     */
    private String operationIp;

    /**
     * 操作UA
     */
    private String operationUa;

    /**
     * 操作结果（0:成功 1:失败）
     */
    private Integer operationResult;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
}

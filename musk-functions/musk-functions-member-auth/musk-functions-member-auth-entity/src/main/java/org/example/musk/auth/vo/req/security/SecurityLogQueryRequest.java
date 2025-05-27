package org.example.musk.auth.vo.req.security;

import lombok.Data;
import org.example.musk.auth.enums.SecurityOperationEnum;

import java.time.LocalDateTime;

/**
 * 安全日志查询请求
 *
 * @author musk
 */
@Data
public class SecurityLogQueryRequest {

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 20;

    /**
     * 操作类型
     */
    private SecurityOperationEnum operationType;

    /**
     * 操作结果（SUCCESS/FAILED）
     */
    private String operationResult;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 客户端IP
     */
    private String clientIp;
}

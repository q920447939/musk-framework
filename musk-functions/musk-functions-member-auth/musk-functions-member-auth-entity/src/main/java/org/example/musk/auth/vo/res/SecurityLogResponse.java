package org.example.musk.auth.vo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 安全日志响应
 *
 * @author musk
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityLogResponse {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 操作类型编码
     */
    private String operationType;

    /**
     * 操作类型描述
     */
    private String operationDesc;

    /**
     * 操作结果
     */
    private String operationResult;

    /**
     * 操作结果描述
     */
    private String operationResultDesc;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 用户代理信息
     */
    private String userAgent;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 失败原因（操作失败时显示）
     */
    private String failureReason;

    /**
     * 设备信息（从userAgent解析）
     */
    private String deviceInfo;

    /**
     * 地理位置信息（从IP解析）
     */
    private String locationInfo;
}

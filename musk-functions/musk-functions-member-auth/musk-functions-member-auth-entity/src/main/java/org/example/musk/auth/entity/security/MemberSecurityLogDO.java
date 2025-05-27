package org.example.musk.auth.entity.security;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.musk.common.pojo.db.DomainBaseDO;

/**
 * 会员安全操作日志 DO
 *
 * @author musk
 */
@TableName("member_security_log")
@KeySequence("member_security_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSecurityLogDO extends DomainBaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 操作类型（对应 SecurityOperationEnum）
     */
    private String operationType;

    /**
     * 操作结果（SUCCESS/FAILED）
     */
    private String operationResult;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 用户代理信息
     */
    private String userAgent;

    /**
     * 扩展信息（JSON格式）
     */
    private String extraInfo;

    /**
     * 失败原因（操作失败时记录）
     */
    private String failureReason;

    /**
     * 操作描述
     */
    private String operationDesc;
}

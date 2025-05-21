package org.example.musk.functions.member.level.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 积分变更事件
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointsChangeEvent {
    
    /**
     * 会员ID
     */
    private Integer memberId;
    
    /**
     * 变更类型
     */
    private Integer changeType;
    
    /**
     * 变更值
     */
    private Integer changeValue;
    
    /**
     * 变更前值
     */
    private Integer beforeValue;
    
    /**
     * 变更后值
     */
    private Integer afterValue;
    
    /**
     * 来源类型
     */
    private Integer sourceType;
    
    /**
     * 来源ID
     */
    private String sourceId;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 租户ID
     */
    private Integer tenantId;
    
    /**
     * 域ID
     */
    private Integer domainId;
}

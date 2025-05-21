package org.example.musk.functions.member.level.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员等级变更事件
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelChangeEvent {
    
    /**
     * 会员ID
     */
    private Integer memberId;
    
    /**
     * 旧等级ID
     */
    private Integer oldLevelId;
    
    /**
     * 新等级ID
     */
    private Integer newLevelId;
    
    /**
     * 变更类型
     */
    private Integer changeType;
    
    /**
     * 变更原因
     */
    private String changeReason;
    
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

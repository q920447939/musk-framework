package org.example.musk.functions.member.level.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会员等级变更记录 VO
 *
 * @author musk-functions-member-level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelChangeRecordVO {
    
    /**
     * 记录ID
     */
    private Integer id;
    
    /**
     * 会员ID
     */
    private Integer memberId;
    
    /**
     * 旧等级ID
     */
    private Integer oldLevelId;
    
    /**
     * 旧等级名称
     */
    private String oldLevelName;
    
    /**
     * 新等级ID
     */
    private Integer newLevelId;
    
    /**
     * 新等级名称
     */
    private String newLevelName;
    
    /**
     * 变更类型(1:升级 2:降级 3:初始化)
     */
    private Integer changeType;
    
    /**
     * 变更类型名称
     */
    private String changeTypeName;
    
    /**
     * 变更原因
     */
    private String changeReason;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

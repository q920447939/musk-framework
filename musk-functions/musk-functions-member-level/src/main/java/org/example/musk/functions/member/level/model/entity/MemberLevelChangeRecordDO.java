package org.example.musk.functions.member.level.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;

/**
 * 会员等级变更记录 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_level_change_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelChangeRecordDO extends DomainBaseDO {

    /**
     * 记录ID
     */
    @TableId
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
     * 新等级ID
     */
    private Integer newLevelId;

    /**
     * 变更类型(1:升级 2:降级 3:初始化)
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
}

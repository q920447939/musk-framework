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
 * 成长值变更记录 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_growth_value_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberGrowthValueRecordDO extends DomainBaseDO {

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
     * 变更类型(1:增加 2:减少)
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
     * 来源类型(1:消费 2:活动 3:签到 4:任务 5:管理员调整 6:其他)
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
}

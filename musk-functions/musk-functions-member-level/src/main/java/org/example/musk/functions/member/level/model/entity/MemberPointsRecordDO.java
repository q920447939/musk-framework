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

import java.time.LocalDateTime;

/**
 * 积分变更记录 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_points_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointsRecordDO extends DomainBaseDO {

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
     * 变更类型(1:增加 2:减少 3:冻结 4:解冻 5:过期)
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
     * 来源类型(1:消费 2:活动 3:签到 4:任务 5:兑换 6:退款 7:管理员调整 8:其他)
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
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 操作人
     */
    private String operator;
}

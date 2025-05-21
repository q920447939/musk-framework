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
 * 会员积分 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_points")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPointsDO extends DomainBaseDO {

    /**
     * 积分ID
     */
    @TableId
    private Integer id;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 可用积分
     */
    private Integer availablePoints;

    /**
     * 冻结积分
     */
    private Integer frozenPoints;

    /**
     * 总积分(累计获得)
     */
    private Integer totalPoints;

    /**
     * 已使用积分
     */
    private Integer usedPoints;

    /**
     * 已过期积分
     */
    private Integer expiredPoints;
}

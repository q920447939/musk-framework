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
 * 会员成长值 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_growth_value")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberGrowthValueDO extends DomainBaseDO {

    /**
     * 成长值ID
     */
    @TableId
    private Integer id;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 当前等级ID
     */
    private Integer currentLevelId;

    /**
     * 总成长值
     */
    private Integer totalGrowthValue;

    /**
     * 当前周期成长值
     */
    private Integer currentPeriodGrowthValue;

    /**
     * 下一等级门槛
     */
    private Integer nextLevelThreshold;

    /**
     * 周期开始时间
     */
    private LocalDateTime periodStartTime;

    /**
     * 周期结束时间
     */
    private LocalDateTime periodEndTime;
}

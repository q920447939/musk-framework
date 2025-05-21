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
 * 会员等级权益 DO
 *
 * @author musk-functions-member-level
 */
@TableName("member_level_benefit")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLevelBenefitDO extends DomainBaseDO {

    /**
     * 权益ID
     */
    @TableId
    private Integer id;

    /**
     * 等级ID
     */
    private Integer levelId;

    /**
     * 权益类型(1:折扣率 2:免邮次数 3:生日礼 4:专属客服 5:积分加速 6:自定义权益)
     */
    private Integer benefitType;

    /**
     * 权益名称
     */
    private String benefitName;

    /**
     * 权益值
     */
    private String benefitValue;

    /**
     * 权益图标ID
     */
    private Integer benefitIconId;

    /**
     * 权益描述
     */
    private String benefitDescription;

    /**
     * 显示顺序
     */
    private Integer displayIndex;

    /**
     * 状态(0:启用 1:禁用)
     */
    private Integer status;
}

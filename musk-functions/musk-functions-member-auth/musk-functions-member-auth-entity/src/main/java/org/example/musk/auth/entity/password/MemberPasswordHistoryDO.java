package org.example.musk.auth.entity.password;

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
 * 会员密码历史记录 DO
 *
 * @author musk
 */
@TableName("member_password_history")
@KeySequence("member_password_history_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPasswordHistoryDO extends DomainBaseDO {

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
     * 密码哈希值（加密后的密码）
     */
    private String passwordHash;

    /**
     * 密码创建时间（使用父类的createTime字段记录）
     */
    // 使用父类的 createTime 字段

    /**
     * 备注信息
     */
    private String remark;
}

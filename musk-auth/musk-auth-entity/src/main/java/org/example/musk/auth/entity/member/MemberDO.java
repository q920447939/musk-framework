package org.example.musk.auth.entity.member;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.musk.common.pojo.db.DomainBaseDO;
import org.example.musk.common.pojo.db.TenantBaseDO;

/**
 * 会员 DO
 *
 * @author musk
 */
@TableName("member")
@KeySequence("member_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDO extends DomainBaseDO {

    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * 注册渠道
     *
     */
    private Integer registerChannel;

    /**
     * 会员账号
     */
    private String memberCode;

    /**
     * 会员昵称
     */
    private String memberNickName;
    /**
     * 密码
     */
    private String password;

    /**
     * 会员识别id
     */
    private Integer memberSimpleId;
    /**
     * 邀请会员识别id
     */
    private int inviteMemberSimpleId;


    /**
     * 头像地址
     */
    private String avatar;


    /**
     * 状态枚举
     */
    private Integer status;

}

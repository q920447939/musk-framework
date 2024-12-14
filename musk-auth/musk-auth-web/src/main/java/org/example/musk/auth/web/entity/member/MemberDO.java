package org.example.musk.auth.web.entity.member;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.musk.common.pojo.db.BaseDO;

/**
 * 会员表 DO
 *
 * @author 代码生成器
 */
@TableName("member")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDO extends BaseDO {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Integer tenantId;

    /**
     * 注册渠道
     */
    @TableField("register_channel")
    private Integer registerChannel;

    /**
     * 会员账号
     */
    @TableField("member_code")
    private String memberCode;

    /**
     * 会员昵称
     */
    @TableField("member_nick_name")
    private String memberNickName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 会员识别id
     */
    @TableField("member_simple_id")
    private Integer memberSimpleId;

    /**
     * 邀请会员识别id
     */
    @TableField("invite_member_simple_id")
    private Integer inviteMemberSimpleId;

    /**
     * 头像地址
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 状态枚举
     */
    @TableField("status")
    private Integer status;

}
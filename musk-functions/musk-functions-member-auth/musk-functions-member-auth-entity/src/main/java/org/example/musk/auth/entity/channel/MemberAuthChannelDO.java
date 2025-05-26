package org.example.musk.auth.entity.channel;

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

import java.time.LocalDateTime;

/**
 * 会员认证渠道 DO
 *
 * @author musk
 */

/**
 * @Builder 只处理当前类的字段
 * @SuperBuilder 支持继承链，会包含父类的所有字段
 * 使用 @SuperBuilder 时，整个继承链都需要使用 @SuperBuilder
 * 需要添加 @NoArgsConstructor 因为 @SuperBuilder 不会自动生成无参构造函数
 */
@TableName("member_auth_channel")
@KeySequence("member_auth_channel_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthChannelDO extends DomainBaseDO {

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
     * 认证渠道类型
     * 对应 AuthChannelTypeEnum
     */
    private String channelType;

    /**
     * 渠道标识
     * 用户名/邮箱地址/手机号/第三方openId等
     */
    private String channelIdentifier;

    /**
     * 渠道值
     * 加密后的密码/第三方用户信息JSON等
     */
    private String channelValue;

    /**
     * 是否已验证
     */
    private Boolean isVerified;

    /**
     * 是否为主要认证方式
     */
    private Boolean isPrimary;

    /**
     * 验证时间
     */
    private LocalDateTime verifiedTime;

    /**
     * 最后使用时间
     */
    private LocalDateTime lastUsedTime;

    /**
     * 扩展信息
     * JSON格式，存储第三方用户信息、设备指纹等
     */
    private String extraInfo;
}

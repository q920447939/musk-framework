package org.example.musk.auth.service.core.channel;

import org.example.musk.auth.entity.channel.MemberAuthChannelDO;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.enums.auth.AuthChannelTypeEnum;

import java.util.List;

/**
 * 会员认证渠道服务接口
 * 
 * @author musk
 */
public interface MemberAuthChannelService {

    /**
     * 添加认证渠道
     *
     * @param memberId 会员ID
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @param value 渠道值
     * @param isVerified 是否已验证
     * @return 认证渠道ID
     */
    Long addAuthChannel(Integer memberId, AuthChannelTypeEnum channelType, 
                       String identifier, String value, boolean isVerified);

    /**
     * 添加认证渠道（带扩展信息）
     *
     * @param memberId 会员ID
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @param value 渠道值
     * @param isVerified 是否已验证
     * @param extraInfo 扩展信息
     * @return 认证渠道ID
     */
    Long addAuthChannel(Integer memberId, AuthChannelTypeEnum channelType, 
                       String identifier, String value, boolean isVerified, String extraInfo);

    /**
     * 验证认证渠道
     *
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @param value 渠道值
     * @return true-验证成功，false-验证失败
     */
    boolean verifyAuthChannel(AuthChannelTypeEnum channelType, String identifier, String value);

    /**
     * 根据渠道标识查找会员
     *
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @return 会员信息，如果不存在则返回null
     */
    MemberDO findMemberByChannel(AuthChannelTypeEnum channelType, String identifier);

    /**
     * 根据渠道标识查找认证渠道
     *
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @return 认证渠道信息，如果不存在则返回null
     */
    MemberAuthChannelDO findAuthChannel(AuthChannelTypeEnum channelType, String identifier);

    /**
     * 获取会员的所有认证渠道
     *
     * @param memberId 会员ID
     * @return 认证渠道列表
     */
    List<MemberAuthChannelDO> getMemberAuthChannels(Integer memberId);

    /**
     * 获取会员的指定类型认证渠道
     *
     * @param memberId 会员ID
     * @param channelType 渠道类型
     * @return 认证渠道列表
     */
    List<MemberAuthChannelDO> getMemberAuthChannels(Integer memberId, AuthChannelTypeEnum channelType);

    /**
     * 获取会员的主要认证渠道
     *
     * @param memberId 会员ID
     * @return 主要认证渠道，如果不存在则返回null
     */
    MemberAuthChannelDO getPrimaryAuthChannel(Integer memberId);

    /**
     * 设置主要认证方式
     *
     * @param memberId 会员ID
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @return true-设置成功，false-设置失败
     */
    boolean setPrimaryAuthChannel(Integer memberId, AuthChannelTypeEnum channelType, String identifier);

    /**
     * 解绑认证渠道
     *
     * @param memberId 会员ID
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @return true-解绑成功，false-解绑失败
     */
    boolean unbindAuthChannel(Integer memberId, AuthChannelTypeEnum channelType, String identifier);

    /**
     * 更新认证渠道验证状态
     *
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @param isVerified 是否已验证
     * @return true-更新成功，false-更新失败
     */
    boolean updateVerifiedStatus(AuthChannelTypeEnum channelType, String identifier, boolean isVerified);

    /**
     * 更新认证渠道最后使用时间
     *
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @return true-更新成功，false-更新失败
     */
    boolean updateLastUsedTime(AuthChannelTypeEnum channelType, String identifier);

    /**
     * 更新认证渠道值
     *
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @param newValue 新的渠道值
     * @return true-更新成功，false-更新失败
     */
    boolean updateChannelValue(AuthChannelTypeEnum channelType, String identifier, String newValue);

    /**
     * 检查会员是否已绑定指定渠道
     *
     * @param memberId 会员ID
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @return true-已绑定，false-未绑定
     */
    boolean isChannelBound(Integer memberId, AuthChannelTypeEnum channelType, String identifier);

    /**
     * 检查渠道标识是否已被使用
     *
     * @param channelType 渠道类型
     * @param identifier 渠道标识
     * @return true-已被使用，false-未被使用
     */
    boolean isChannelIdentifierUsed(AuthChannelTypeEnum channelType, String identifier);
}

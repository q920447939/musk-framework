package org.example.musk.auth.service.core.password;

import org.example.musk.auth.entity.password.MemberPasswordHistoryDO;

import java.util.List;

/**
 * 会员密码历史记录服务接口
 *
 * @author musk
 */
public interface MemberPasswordHistoryService {

    /**
     * 保存密码历史记录
     *
     * @param memberId 会员ID
     * @param passwordHash 密码哈希值
     * @param remark 备注
     * @return 保存的记录ID
     */
    Long savePasswordHistory(Integer memberId, String passwordHash, String remark);

    /**
     * 获取会员的密码历史记录
     *
     * @param memberId 会员ID
     * @param limit 获取数量限制
     * @return 密码历史记录列表
     */
    List<MemberPasswordHistoryDO> getPasswordHistory(Integer memberId, Integer limit);

    /**
     * 检查密码是否在历史记录中
     *
     * @param memberId 会员ID
     * @param passwordHash 密码哈希值
     * @param checkCount 检查最近几次记录
     * @return true-存在，false-不存在
     */
    boolean isPasswordInHistory(Integer memberId, String passwordHash, Integer checkCount);

    /**
     * 清理过期的密码历史记录
     *
     * @param memberId 会员ID
     * @param keepCount 保留记录数量
     * @return 清理的记录数量
     */
    int cleanExpiredHistory(Integer memberId, Integer keepCount);

    /**
     * 删除会员的所有密码历史记录
     *
     * @param memberId 会员ID
     * @return 删除的记录数量
     */
    int deleteAllHistory(Integer memberId);
}

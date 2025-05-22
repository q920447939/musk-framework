package org.example.musk.functions.member.level.service;

import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.member.level.enums.PointsSourceTypeEnum;
import org.example.musk.functions.member.level.model.vo.MemberPointsRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsVO;

/**
 * 会员积分服务接口
 *
 * @author musk-functions-member-level
 */
public interface MemberPointsService {

    /**
     * 增加会员积分
     *
     * @param memberId 会员ID
     * @param points 积分值
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @param operator 操作人
     * @return 操作后的积分
     */
    Integer addPoints(Integer memberId, Integer points, PointsSourceTypeEnum sourceType, String sourceId, String description, String operator);

    /**
     * 减少会员积分
     *
     * @param memberId 会员ID
     * @param points 积分值
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @param operator 操作人
     * @return 操作后的积分
     */
    Integer deductPoints(Integer memberId, Integer points, PointsSourceTypeEnum sourceType, String sourceId, String description, String operator);

    /**
     * 冻结会员积分
     *
     * @param memberId 会员ID
     * @param points 积分值
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @param operator 操作人
     * @return 操作后的冻结积分
     */
    Integer freezePoints(Integer memberId, Integer points, PointsSourceTypeEnum sourceType, String sourceId, String description, String operator);

    /**
     * 解冻会员积分
     *
     * @param memberId 会员ID
     * @param points 积分值
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @param operator 操作人
     * @return 操作后的可用积分
     */
    Integer unfreezePoints(Integer memberId, Integer points, PointsSourceTypeEnum sourceType, String sourceId, String description, String operator);

    /**
     * 过期会员积分
     *
     * @param memberId 会员ID
     * @param points 积分值
     * @param sourceType 来源类型
     * @param sourceId 来源ID
     * @param description 描述
     * @param operator 操作人
     * @return 操作后的可用积分
     */
    Integer expirePoints(Integer memberId, Integer points, PointsSourceTypeEnum sourceType, String sourceId, String description, String operator);

    /**
     * 获取会员积分信息
     *
     * @param memberId 会员ID
     * @return 积分信息
     */
    MemberPointsVO getMemberPoints(Integer memberId);

    /**
     * 获取会员积分变更历史
     *
     * @param memberId 会员ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 变更历史分页结果
     */
    PageResult<MemberPointsRecordVO> getMemberPointsHistory(Integer memberId, Integer pageNum, Integer pageSize);
}

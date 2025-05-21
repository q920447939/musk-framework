package org.example.musk.functions.member.level.service;

import org.example.musk.functions.member.level.model.entity.MemberPointsRuleDO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRuleUpdateReqVO;

import java.util.List;

/**
 * 积分规则服务接口
 *
 * @author musk-functions-member-level
 */
public interface MemberPointsRuleService {

    /**
     * 创建积分规则
     *
     * @param createReqVO 创建请求
     * @return 规则ID
     */
    Integer createPointsRule(Integer tenantId,Integer domainId,MemberPointsRuleCreateReqVO createReqVO);

    /**
     * 更新积分规则
     *
     * @param updateReqVO 更新请求
     */
    void updatePointsRule(Integer tenantId,Integer domainId,MemberPointsRuleUpdateReqVO updateReqVO);

    /**
     * 删除积分规则
     *
     * @param id 规则ID
     */
    void deletePointsRule(Integer tenantId,Integer domainId,Integer id);

    /**
     * 获取积分规则
     *
     * @param id 规则ID
     * @return 规则信息
     */
    MemberPointsRuleDO getPointsRule(Integer id);

    /**
     * 获取积分规则列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 规则列表
     */
    List<MemberPointsRuleDO> getPointsRuleList(Integer tenantId, Integer domainId);

    /**
     * 计算消费积分和成长值
     *
     * @param memberId 会员ID
     * @param amount 消费金额（分）
     * @param sourceId 订单ID
     * @param operator 操作人
     */
    void calculateConsumptionPointsAndGrowth(Integer tenantId,Integer domainId,Integer memberId, Integer amount, String sourceId, String operator);

    /**
     * 计算签到积分和成长值
     *
     * @param memberId 会员ID
     * @param continuousDays 连续签到天数
     * @param operator 操作人
     */
    void calculateSignInPointsAndGrowth(Integer tenantId,Integer domainId,Integer memberId, Integer continuousDays, String operator);
}

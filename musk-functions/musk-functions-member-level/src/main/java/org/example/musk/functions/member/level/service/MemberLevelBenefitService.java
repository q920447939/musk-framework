package org.example.musk.functions.member.level.service;

import org.example.musk.functions.member.level.model.entity.MemberLevelBenefitDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitVO;

import java.util.List;

/**
 * 会员等级权益服务接口
 *
 * @author musk-functions-member-level
 */
public interface MemberLevelBenefitService {

    /**
     * 创建会员等级权益
     *
     * @param createReqVO 创建请求
     * @return 权益ID
     */
    Integer createLevelBenefit(MemberLevelBenefitCreateReqVO createReqVO);

    /**
     * 更新会员等级权益
     *
     * @param updateReqVO 更新请求
     */
    void updateLevelBenefit(MemberLevelBenefitUpdateReqVO updateReqVO);

    /**
     * 删除会员等级权益
     *
     * @param id 权益ID
     */
    void deleteLevelBenefit(Integer id);

    /**
     * 获取会员等级权益
     *
     * @param id 权益ID
     * @return 权益信息
     */
    MemberLevelBenefitDO getLevelBenefit(Integer id);

    /**
     * 获取会员等级权益列表
     *
     * @param levelId 等级ID
     * @return 权益列表
     */
    List<MemberLevelBenefitDO> getLevelBenefitList(Integer levelId);

    /**
     * 获取会员当前权益列表
     *
     * @param memberId 会员ID
     * @return 权益列表
     */
    List<MemberLevelBenefitVO> getMemberCurrentBenefits(Integer memberId);

    /**
     * 检查会员是否拥有特定权益
     *
     * @param memberId 会员ID
     * @param benefitType 权益类型
     * @return 是否拥有
     */
    boolean hasBenefit(Integer memberId, Integer benefitType);

    /**
     * 获取会员特定权益的值
     *
     * @param memberId 会员ID
     * @param benefitType 权益类型
     * @return 权益值
     */
    String getBenefitValue(Integer memberId, Integer benefitType);
}

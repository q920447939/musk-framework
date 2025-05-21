package org.example.musk.functions.member.level.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberLevelBenefitDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

import java.util.List;

/**
 * 会员等级权益 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberLevelBenefitMapper extends BaseMapperX<MemberLevelBenefitDO> {

    /**
     * 根据等级ID获取会员等级权益列表
     *
     * @param levelId 等级ID
     * @return 会员等级权益列表
     */
    default List<MemberLevelBenefitDO> selectListByLevelId(Integer levelId) {
        return selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberLevelBenefitDO>()
                        .eq(MemberLevelBenefitDO::getLevelId, levelId)
                        .eq(MemberLevelBenefitDO::getStatus, 0)
                        .eq(MemberLevelBenefitDO::getDeleted, false)
                        .orderByAsc(MemberLevelBenefitDO::getDisplayIndex)
        );
    }

    /**
     * 根据等级ID和权益类型获取会员等级权益
     *
     * @param levelId     等级ID
     * @param benefitType 权益类型
     * @return 会员等级权益
     */
    default MemberLevelBenefitDO selectByLevelIdAndBenefitType(Integer levelId, Integer benefitType) {
        return selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberLevelBenefitDO>()
                        .eq(MemberLevelBenefitDO::getLevelId, levelId)
                        .eq(MemberLevelBenefitDO::getBenefitType, benefitType)
                        .eq(MemberLevelBenefitDO::getStatus, 0)
                        .eq(MemberLevelBenefitDO::getDeleted, false)
        );
    }

    /**
     * 根据等级ID删除会员等级权益
     *
     * @param levelId 等级ID
     * @return 删除数量
     */
    default int deleteByLevelId(Integer levelId) {
        return delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberLevelBenefitDO>()
                        .eq(MemberLevelBenefitDO::getLevelId, levelId)
        );
    }
}

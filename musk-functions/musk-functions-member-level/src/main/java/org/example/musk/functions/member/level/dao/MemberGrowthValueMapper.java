package org.example.musk.functions.member.level.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberGrowthValueDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

/**
 * 会员成长值 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberGrowthValueMapper extends BaseMapperX<MemberGrowthValueDO> {

    /**
     * 根据会员ID获取会员成长值
     *
     * @param memberId 会员ID
     * @return 会员成长值
     */
    default MemberGrowthValueDO selectByMemberId(Integer domainId,Integer memberId) {
        return selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberGrowthValueDO>()
                        .eq(MemberGrowthValueDO::getDomainId, domainId)
                        .eq(MemberGrowthValueDO::getMemberId, memberId)
                        .eq(MemberGrowthValueDO::getDeleted, false)
        );
    }

    /**
     * 根据等级ID获取会员数量
     *
     * @param levelId 等级ID
     * @return 会员数量
     */
    default Long countByLevelId(Integer levelId) {
        return selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberGrowthValueDO>()
                        .eq(MemberGrowthValueDO::getCurrentLevelId, levelId)
                        .eq(MemberGrowthValueDO::getDeleted, false)
        );
    }
}

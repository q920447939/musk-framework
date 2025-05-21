package org.example.musk.functions.member.level.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberPointsDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

/**
 * 会员积分 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberPointsMapper extends BaseMapperX<MemberPointsDO> {

    /**
     * 根据会员ID获取会员积分
     *
     * @param memberId 会员ID
     * @return 会员积分
     */
    default MemberPointsDO selectByMemberId(Integer memberId) {
        return selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberPointsDO>()
                        .eq(MemberPointsDO::getMemberId, memberId)
                        .eq(MemberPointsDO::getDeleted, false)
        );
    }
}

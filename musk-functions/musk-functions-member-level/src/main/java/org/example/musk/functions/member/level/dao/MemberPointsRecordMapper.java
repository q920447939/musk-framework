package org.example.musk.functions.member.level.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberPointsRecordDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

/**
 * 积分变更记录 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberPointsRecordMapper extends BaseMapperX<MemberPointsRecordDO> {

    /**
     * 根据会员ID分页获取积分变更记录
     *
     * @param memberId 会员ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 积分变更记录分页
     */
    default Page<MemberPointsRecordDO> selectPageByMemberId(Integer memberId, Integer pageNum, Integer pageSize) {
        return selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<MemberPointsRecordDO>()
                        .eq(MemberPointsRecordDO::getMemberId, memberId)
                        .eq(MemberPointsRecordDO::getDeleted, false)
                        .orderByDesc(MemberPointsRecordDO::getCreateTime)
        );
    }
}

package org.example.musk.functions.member.level.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberGrowthValueRecordDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

/**
 * 成长值变更记录 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberGrowthValueRecordMapper extends BaseMapperX<MemberGrowthValueRecordDO> {

    /**
     * 根据会员ID分页获取成长值变更记录
     *
     * @param memberId 会员ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 成长值变更记录分页
     */
    default Page<MemberGrowthValueRecordDO> selectPageByMemberId(Integer memberId, Integer pageNum, Integer pageSize) {
        return selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<MemberGrowthValueRecordDO>()
                        .eq(MemberGrowthValueRecordDO::getMemberId, memberId)
                        .eq(MemberGrowthValueRecordDO::getDeleted, false)
                        .orderByDesc(MemberGrowthValueRecordDO::getCreateTime)
        );
    }
}

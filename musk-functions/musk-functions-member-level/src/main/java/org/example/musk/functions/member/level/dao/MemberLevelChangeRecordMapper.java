package org.example.musk.functions.member.level.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberLevelChangeRecordDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

/**
 * 会员等级变更记录 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberLevelChangeRecordMapper extends BaseMapperX<MemberLevelChangeRecordDO> {

    /**
     * 根据会员ID分页获取会员等级变更记录
     *
     * @param memberId 会员ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 会员等级变更记录分页
     */
    default Page<MemberLevelChangeRecordDO> selectPageByMemberId(Integer memberId, Integer pageNum, Integer pageSize) {
        return selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<MemberLevelChangeRecordDO>()
                        .eq(MemberLevelChangeRecordDO::getMemberId, memberId)
                        .eq(MemberLevelChangeRecordDO::getDeleted, false)
                        .orderByDesc(MemberLevelChangeRecordDO::getCreateTime)
        );
    }
}

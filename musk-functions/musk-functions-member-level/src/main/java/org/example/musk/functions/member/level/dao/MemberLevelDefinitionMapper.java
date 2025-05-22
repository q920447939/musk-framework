package org.example.musk.functions.member.level.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

import java.util.List;

/**
 * 会员等级定义 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberLevelDefinitionMapper extends BaseMapperX<MemberLevelDefinitionDO> {

    /**
     * 根据租户ID和域ID获取会员等级定义列表
     *
     * @return 会员等级定义列表
     */
    default List<MemberLevelDefinitionDO> selectList() {
        return selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberLevelDefinitionDO>()
                        .eq(MemberLevelDefinitionDO::getDeleted, false)
                        .orderByAsc(MemberLevelDefinitionDO::getLevelValue)
        );
    }

    /**
     * 根据等级编码获取会员等级定义
     *
     * @param levelCode 等级编码
     * @return 会员等级定义
     */
    default MemberLevelDefinitionDO selectByLevelCode( String levelCode) {
        return selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberLevelDefinitionDO>()
                        .eq(MemberLevelDefinitionDO::getLevelCode, levelCode)
                        .eq(MemberLevelDefinitionDO::getDeleted, false)
        );
    }

    /**
     * 根据成长值获取对应的会员等级定义
     *
     * @param growthValue  成长值
     * @return 会员等级定义
     */
    default MemberLevelDefinitionDO selectByGrowthValue(  Integer growthValue) {
        return selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberLevelDefinitionDO>()
                        .eq(MemberLevelDefinitionDO::getStatus, 0)
                        .le(MemberLevelDefinitionDO::getGrowthValueThreshold, growthValue)
                        .orderByDesc(MemberLevelDefinitionDO::getGrowthValueThreshold)
                        .last("LIMIT 1")
        );
    }

    /**
     * 获取下一个等级
     *
     * @param currentLevelValue 当前等级值
     * @return 下一个等级
     */
    default MemberLevelDefinitionDO selectNextLevel(  Integer currentLevelValue) {
        return selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberLevelDefinitionDO>()
                        .eq(MemberLevelDefinitionDO::getStatus, 0)
                        .eq(MemberLevelDefinitionDO::getDeleted, false)
                        .gt(MemberLevelDefinitionDO::getLevelValue, currentLevelValue)
                        .orderByAsc(MemberLevelDefinitionDO::getLevelValue)
                        .last("LIMIT 1")
        );
    }
}

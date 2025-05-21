package org.example.musk.functions.member.level.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.musk.functions.member.level.model.entity.MemberPointsRuleDO;
import org.example.musk.middleware.mybatisplus.mybatis.core.mapper.BaseMapperX;

import java.util.List;

/**
 * 积分规则 Mapper
 *
 * @author musk-functions-member-level
 */
@Mapper
public interface MemberPointsRuleMapper extends BaseMapperX<MemberPointsRuleDO> {

    /**
     * 根据租户ID和域ID获取积分规则列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @return 积分规则列表
     */
    default List<MemberPointsRuleDO> selectListByTenantIdAndDomainId(Integer tenantId, Integer domainId) {
        return selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberPointsRuleDO>()
                        .eq(MemberPointsRuleDO::getTenantId, tenantId)
                        .eq(MemberPointsRuleDO::getDomainId, domainId)
                        .eq(MemberPointsRuleDO::getDeleted, false)
                        .orderByAsc(MemberPointsRuleDO::getRuleType)
        );
    }

    /**
     * 根据规则编码获取积分规则
     *
     * @param tenantId  租户ID
     * @param domainId  域ID
     * @param ruleCode 规则编码
     * @return 积分规则
     */
    default MemberPointsRuleDO selectByRuleCode(Integer tenantId, Integer domainId, String ruleCode) {
        return selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberPointsRuleDO>()
                        .eq(MemberPointsRuleDO::getTenantId, tenantId)
                        .eq(MemberPointsRuleDO::getDomainId, domainId)
                        .eq(MemberPointsRuleDO::getRuleCode, ruleCode)
                        .eq(MemberPointsRuleDO::getDeleted, false)
        );
    }

    /**
     * 根据规则类型获取积分规则列表
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     * @param ruleType 规则类型
     * @return 积分规则列表
     */
    default List<MemberPointsRuleDO> selectListByRuleType(Integer tenantId, Integer domainId, Integer ruleType) {
        return selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MemberPointsRuleDO>()
                        .eq(MemberPointsRuleDO::getTenantId, tenantId)
                        .eq(MemberPointsRuleDO::getDomainId, domainId)
                        .eq(MemberPointsRuleDO::getRuleType, ruleType)
                        .eq(MemberPointsRuleDO::getStatus, 0)
                        .eq(MemberPointsRuleDO::getDeleted, false)
        );
    }
}

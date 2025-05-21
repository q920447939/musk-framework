package org.example.musk.functions.member.level.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.member.level.constant.MemberLevelConstant;
import org.example.musk.functions.member.level.dao.MemberGrowthValueMapper;
import org.example.musk.functions.member.level.dao.MemberLevelBenefitMapper;
import org.example.musk.functions.member.level.dao.MemberLevelDefinitionMapper;
import org.example.musk.functions.member.level.enums.LevelBenefitTypeEnum;
import org.example.musk.functions.member.level.model.entity.MemberGrowthValueDO;
import org.example.musk.functions.member.level.model.entity.MemberLevelBenefitDO;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelBenefitVO;
import org.example.musk.functions.member.level.service.MemberLevelBenefitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员等级权益服务实现类
 *
 * @author musk-functions-member-level
 */
@Service
@Slf4j
public class MemberLevelBenefitServiceImpl implements MemberLevelBenefitService {

    @Resource
    private MemberLevelBenefitMapper memberLevelBenefitMapper;

    @Resource
    private MemberLevelDefinitionMapper memberLevelDefinitionMapper;

    @Resource
    private MemberGrowthValueMapper memberGrowthValueMapper;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createLevelBenefit(MemberLevelBenefitCreateReqVO createReqVO) {

        // 检查等级是否存在
        MemberLevelDefinitionDO levelDefinition = memberLevelDefinitionMapper.selectById(createReqVO.getLevelId());
        if (levelDefinition == null) {
            throw new BusinessException("等级不存在");
        }

        // 检查权益类型是否已存在
        MemberLevelBenefitDO existBenefit = memberLevelBenefitMapper.selectByLevelIdAndBenefitType(
                createReqVO.getLevelId(), createReqVO.getBenefitType());
        if (existBenefit != null) {
            throw new BusinessException("该等级下已存在相同类型的权益");
        }

        // 创建权益
        MemberLevelBenefitDO benefit = BeanUtil.copyProperties(createReqVO, MemberLevelBenefitDO.class);
        memberLevelBenefitMapper.insert(benefit);

        // 清除缓存
        clearLevelBenefitListCache(createReqVO.getLevelId());

        return benefit.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLevelBenefit(MemberLevelBenefitUpdateReqVO updateReqVO) {

        // 检查权益是否存在
        MemberLevelBenefitDO benefit = memberLevelBenefitMapper.selectById(updateReqVO.getId());
        if (benefit == null) {
            throw new BusinessException("权益不存在");
        }

        // 检查等级是否存在
        MemberLevelDefinitionDO levelDefinition = memberLevelDefinitionMapper.selectById(updateReqVO.getLevelId());
        if (levelDefinition == null) {
            throw new BusinessException("等级不存在");
        }


        // 如果修改了等级ID或权益类型，需要检查是否已存在
        if (!benefit.getLevelId().equals(updateReqVO.getLevelId()) || !benefit.getBenefitType().equals(updateReqVO.getBenefitType())) {
            MemberLevelBenefitDO existBenefit = memberLevelBenefitMapper.selectByLevelIdAndBenefitType(
                    updateReqVO.getLevelId(), updateReqVO.getBenefitType());
            if (existBenefit != null && !existBenefit.getId().equals(updateReqVO.getId())) {
                throw new BusinessException("该等级下已存在相同类型的权益");
            }
        }

        // 更新权益
        MemberLevelBenefitDO updateBenefit = BeanUtil.copyProperties(updateReqVO, MemberLevelBenefitDO.class);
        memberLevelBenefitMapper.updateById(updateBenefit);

        // 清除缓存
        clearLevelBenefitListCache(benefit.getLevelId());
        if (!benefit.getLevelId().equals(updateReqVO.getLevelId())) {
            clearLevelBenefitListCache(updateReqVO.getLevelId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLevelBenefit(Integer id) {
        // 检查权益是否存在
        MemberLevelBenefitDO benefit = memberLevelBenefitMapper.selectById(id);
        if (benefit == null) {
            throw new BusinessException("权益不存在");
        }
        // 删除权益
        memberLevelBenefitMapper.deleteById(id);

        // 清除缓存
        clearLevelBenefitListCache(benefit.getLevelId());
    }

    @Override
    public MemberLevelBenefitDO getLevelBenefit(Integer id) {
        return memberLevelBenefitMapper.selectById(id);
    }

    @Override
    @Cacheable(namespace = "MEMBER", key = "'benefit:list:' + #levelId", expireSeconds = MemberLevelConstant.LEVEL_BENEFIT_LIST_CACHE_EXPIRE_SECONDS, autoTenantPrefix = true, autoDomainPrefix = true)
    public List<MemberLevelBenefitDO> getLevelBenefitList(Integer levelId) {
        // 从数据库中获取
        return memberLevelBenefitMapper.selectListByLevelId(levelId);
    }

    @Override
    public List<MemberLevelBenefitVO> getMemberCurrentBenefits(Integer memberId) {
        // 从线程上下文获取域ID
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        // 获取会员成长值信息
        MemberGrowthValueDO growthValue = memberGrowthValueMapper.selectByMemberId( memberId);
        if (growthValue == null) {
            throw new BusinessException("会员成长值信息不存在");
        }

        // 获取会员当前等级
        Integer currentLevelId = growthValue.getCurrentLevelId();
        if (currentLevelId == null) {
            return new ArrayList<>();
        }

        // 获取当前等级权益列表
        List<MemberLevelBenefitDO> benefits = getLevelBenefitList(currentLevelId);
        if (CollUtil.isEmpty(benefits)) {
            return new ArrayList<>();
        }

        // 转换为VO
        List<MemberLevelBenefitVO> benefitVOs = new ArrayList<>();
        for (MemberLevelBenefitDO benefit : benefits) {
            MemberLevelBenefitVO vo = BeanUtil.copyProperties(benefit, MemberLevelBenefitVO.class);

            // 设置权益类型名称
            LevelBenefitTypeEnum benefitType = LevelBenefitTypeEnum.getByValue(benefit.getBenefitType());
            if (benefitType != null) {
                vo.setBenefitTypeName(benefitType.getName());
            }

            benefitVOs.add(vo);
        }

        return benefitVOs;
    }

    @Override
    public boolean hasBenefit(Integer memberId, Integer benefitType) {
        // 从线程上下文获取域ID
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        // 获取会员成长值信息
        MemberGrowthValueDO growthValue = memberGrowthValueMapper.selectByMemberId( memberId);
        if (growthValue == null) {
            return false;
        }

        // 获取会员当前等级
        Integer currentLevelId = growthValue.getCurrentLevelId();
        if (currentLevelId == null) {
            return false;
        }

        // 检查是否有指定类型的权益
        MemberLevelBenefitDO benefit = memberLevelBenefitMapper.selectByLevelIdAndBenefitType(currentLevelId, benefitType);
        return benefit != null;
    }

    @Override
    public String getBenefitValue(Integer memberId, Integer benefitType) {

        // 获取会员成长值信息
        MemberGrowthValueDO growthValue = memberGrowthValueMapper.selectByMemberId( memberId);
        if (growthValue == null) {
            return null;
        }

        // 获取会员当前等级
        Integer currentLevelId = growthValue.getCurrentLevelId();
        if (currentLevelId == null) {
            return null;
        }

        // 获取指定类型的权益
        MemberLevelBenefitDO benefit = memberLevelBenefitMapper.selectByLevelIdAndBenefitType(currentLevelId, benefitType);
        if (benefit == null) {
            return null;
        }

        return benefit.getBenefitValue();
    }

    /**
     * 清除等级权益列表缓存
     *
     * @param levelId 等级ID
     */
    @CacheEvict(namespace = "MEMBER", key = "'benefit:list:' + #levelId", autoTenantPrefix = true, autoDomainPrefix = true)
    public void clearLevelBenefitListCache(Integer levelId) {
        // 使用注解自动清除缓存，方法体为空
    }
}

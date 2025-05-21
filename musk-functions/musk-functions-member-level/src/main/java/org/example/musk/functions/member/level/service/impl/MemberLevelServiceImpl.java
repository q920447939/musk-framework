package org.example.musk.functions.member.level.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.member.level.constant.MemberLevelConstant;
import org.example.musk.functions.member.level.dao.MemberGrowthValueMapper;
import org.example.musk.functions.member.level.dao.MemberLevelChangeRecordMapper;
import org.example.musk.functions.member.level.dao.MemberLevelDefinitionMapper;
import org.example.musk.functions.member.level.enums.MemberLevelChangeTypeEnum;
import org.example.musk.functions.member.level.event.MemberLevelChangeEvent;
import org.example.musk.functions.member.level.model.entity.MemberGrowthValueDO;
import org.example.musk.functions.member.level.model.entity.MemberLevelChangeRecordDO;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberLevelChangeRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionCreateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelDefinitionUpdateReqVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelInfoVO;
import org.example.musk.functions.member.level.model.vo.MemberLevelProgressVO;
import org.example.musk.functions.member.level.service.MemberLevelBenefitService;
import org.example.musk.functions.member.level.service.MemberLevelService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员等级服务实现类
 *
 * @author musk-functions-member-level
 */
@Service
@Slf4j
public class MemberLevelServiceImpl implements MemberLevelService {

    @Resource
    private MemberLevelDefinitionMapper memberLevelDefinitionMapper;

    @Resource
    private MemberGrowthValueMapper memberGrowthValueMapper;

    @Resource
    private MemberLevelChangeRecordMapper memberLevelChangeRecordMapper;

    @Resource
    private MemberLevelBenefitService memberLevelBenefitService;


    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createLevelDefinition(Integer tenantId,Integer domainId,MemberLevelDefinitionCreateReqVO createReqVO) {
        // 检查等级编码是否已存在
        MemberLevelDefinitionDO existLevel = memberLevelDefinitionMapper.selectByLevelCode(tenantId, domainId, createReqVO.getLevelCode());
        if (existLevel != null) {
            throw new BusinessException("等级编码已存在");
        }

        // 创建等级定义
        MemberLevelDefinitionDO levelDefinition = BeanUtil.copyProperties(createReqVO, MemberLevelDefinitionDO.class);
        levelDefinition.setTenantId(tenantId);
        levelDefinition.setDomainId(domainId);
        memberLevelDefinitionMapper.insert(levelDefinition);

        // 清除缓存
        clearLevelDefinitionListCache(tenantId, domainId);

        return levelDefinition.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLevelDefinition(Integer tenantId,Integer domainId,MemberLevelDefinitionUpdateReqVO updateReqVO) {
        // 检查等级是否存在
        MemberLevelDefinitionDO levelDefinition = memberLevelDefinitionMapper.selectById(updateReqVO.getId());
        if (levelDefinition == null) {
            throw new BusinessException("等级不存在");
        }

        // 检查是否有权限修改
        if (!ObjectUtil.equal(levelDefinition.getTenantId(), tenantId) || !ObjectUtil.equal(levelDefinition.getDomainId(), domainId)) {
            throw new BusinessException("无权限修改该等级");
        }

        // 检查等级编码是否已存在（排除自身）
        MemberLevelDefinitionDO existLevel = memberLevelDefinitionMapper.selectByLevelCode(tenantId, domainId, updateReqVO.getLevelCode());
        if (existLevel != null && !existLevel.getId().equals(updateReqVO.getId())) {
            throw new BusinessException("等级编码已存在");
        }

        // 更新等级定义
        MemberLevelDefinitionDO updateLevel = BeanUtil.copyProperties(updateReqVO, MemberLevelDefinitionDO.class);
        memberLevelDefinitionMapper.updateById(updateLevel);

        // 清除缓存
        clearLevelDefinitionListCache(tenantId, domainId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLevelDefinition(Integer tenantId,Integer domainId,Integer id) {

        // 检查等级是否存在
        MemberLevelDefinitionDO levelDefinition = memberLevelDefinitionMapper.selectById(id);
        if (levelDefinition == null) {
            throw new BusinessException("等级不存在");
        }


        // 检查是否有会员使用该等级
        Long memberCount = memberGrowthValueMapper.countByLevelId(id);
        if (memberCount > 0) {
            throw new BusinessException("该等级下有会员，无法删除");
        }

        // 删除等级定义
        memberLevelDefinitionMapper.deleteById(id);

        // 清除缓存
        clearLevelDefinitionListCache(tenantId, domainId);
    }

    @Override
    public MemberLevelDefinitionDO getLevelDefinition(Integer id) {
        return memberLevelDefinitionMapper.selectById(id);
    }

    @Override
    public List<MemberLevelDefinitionDO> getLevelDefinitionList(Integer tenantId, Integer domainId) {
        // 从数据库中获取
        return memberLevelDefinitionMapper.selectListByTenantIdAndDomainId(domainId);
    }

    @Override
    @Cacheable(namespace = "MEMBER", key = "'current:' + #memberId", expireSeconds = MemberLevelConstant.MEMBER_CURRENT_LEVEL_CACHE_EXPIRE_SECONDS)
    public MemberLevelInfoVO getMemberCurrentLevel(Integer domainId,Integer memberId) {
        // 获取会员成长值信息
        MemberGrowthValueDO growthValue = memberGrowthValueMapper.selectByMemberId(domainId,memberId);
        if (growthValue == null) {
            throw new BusinessException("会员成长值信息不存在");
        }

        // 获取会员当前等级
        MemberLevelDefinitionDO currentLevel = memberLevelDefinitionMapper.selectById(growthValue.getCurrentLevelId());
        if (currentLevel == null) {
            throw new BusinessException("会员等级不存在");
        }

        // 获取下一等级
        MemberLevelDefinitionDO nextLevel = memberLevelDefinitionMapper.selectNextLevel(
                currentLevel.getDomainId(), currentLevel.getLevelValue());

        // 构建会员等级信息
        MemberLevelInfoVO levelInfo = new MemberLevelInfoVO();
        levelInfo.setMemberId(memberId);
        levelInfo.setCurrentLevelId(currentLevel.getId());
        levelInfo.setCurrentLevelCode(currentLevel.getLevelCode());
        levelInfo.setCurrentLevelName(currentLevel.getLevelName());
        levelInfo.setCurrentLevelIconId(currentLevel.getLevelIconId());
        levelInfo.setCurrentLevelValue(currentLevel.getLevelValue());
        levelInfo.setCurrentLevelColor(currentLevel.getLevelColor());
        levelInfo.setCurrentLevelDescription(currentLevel.getLevelDescription());
        levelInfo.setCurrentGrowthValue(growthValue.getTotalGrowthValue());

        // 设置下一等级信息
        if (nextLevel != null) {
            levelInfo.setNextLevelId(nextLevel.getId());
            levelInfo.setNextLevelName(nextLevel.getLevelName());
            levelInfo.setNextLevelThreshold(nextLevel.getGrowthValueThreshold());

            // 计算距离下一等级还需成长值
            Integer growthValueToNextLevel = nextLevel.getGrowthValueThreshold() - growthValue.getTotalGrowthValue();
            levelInfo.setGrowthValueToNextLevel(Math.max(growthValueToNextLevel, 0));

            // 计算升级进度百分比
            Integer currentThreshold = currentLevel.getGrowthValueThreshold();
            Integer nextThreshold = nextLevel.getGrowthValueThreshold();
            Integer totalGrowthValue = growthValue.getTotalGrowthValue();

            if (nextThreshold > currentThreshold) {
                Integer progress = (totalGrowthValue - currentThreshold) * 100 / (nextThreshold - currentThreshold);
                levelInfo.setUpgradeProgressPercent(Math.min(progress, 100));
            } else {
                levelInfo.setUpgradeProgressPercent(100);
            }
        } else {
            // 已是最高等级
            levelInfo.setUpgradeProgressPercent(100);
        }

        // 获取当前等级权益
        levelInfo.setBenefits(memberLevelBenefitService.getMemberCurrentBenefits(domainId,memberId));

        return levelInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setMemberLevel(Integer tenantId, Integer domainId,Integer memberId, Integer levelId, String reason, String operator) {
        // 获取会员成长值信息
        MemberGrowthValueDO growthValue = memberGrowthValueMapper.selectByMemberId(domainId,memberId);
        if (growthValue == null) {
            throw new BusinessException("会员成长值信息不存在");
        }

        // 获取目标等级
        MemberLevelDefinitionDO targetLevel = memberLevelDefinitionMapper.selectById(levelId);
        if (targetLevel == null) {
            throw new BusinessException("目标等级不存在");
        }

        if (!ObjectUtil.equal(targetLevel.getTenantId(), tenantId) || !ObjectUtil.equal(targetLevel.getDomainId(), domainId)) {
            throw new BusinessException("无权限设置该等级");
        }

        // 记录旧等级
        Integer oldLevelId = growthValue.getCurrentLevelId();

        // 如果等级没有变化，则不处理
        if (ObjectUtil.equal(oldLevelId, levelId)) {
            return;
        }

        // 更新会员成长值表中的当前等级
        growthValue.setCurrentLevelId(levelId);

        // 如果新等级的成长值门槛大于当前成长值，则更新成长值
        if (targetLevel.getGrowthValueThreshold() > growthValue.getTotalGrowthValue()) {
            growthValue.setTotalGrowthValue(targetLevel.getGrowthValueThreshold());
        }

        // 计算下一等级门槛
        MemberLevelDefinitionDO nextLevel = memberLevelDefinitionMapper.selectNextLevel(
                targetLevel.getDomainId(), targetLevel.getLevelValue());
        if (nextLevel != null) {
            growthValue.setNextLevelThreshold(nextLevel.getGrowthValueThreshold());
        } else {
            growthValue.setNextLevelThreshold(null);
        }

        memberGrowthValueMapper.updateById(growthValue);

        // 记录等级变更历史
        MemberLevelChangeRecordDO record = new MemberLevelChangeRecordDO();
        record.setTenantId(growthValue.getTenantId());
        record.setDomainId(growthValue.getDomainId());
        record.setMemberId(memberId);
        record.setOldLevelId(oldLevelId);
        record.setNewLevelId(levelId);
        record.setChangeType(MemberLevelChangeTypeEnum.MANUAL_SET.getValue());
        record.setChangeReason(reason);
        record.setOperator(operator);
        memberLevelChangeRecordMapper.insert(record);

        // 发布会员等级变更事件
        MemberLevelChangeEvent event = MemberLevelChangeEvent.builder()
                .memberId(memberId)
                .oldLevelId(oldLevelId)
                .newLevelId(levelId)
                .changeType(MemberLevelChangeTypeEnum.MANUAL_SET.getValue())
                .changeReason(reason)
                .operator(operator)
                .tenantId(growthValue.getTenantId())
                .domainId(growthValue.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);

        // 清除缓存
        clearMemberCurrentLevelCache(memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer calculateMemberLevel(Integer domainId,Integer memberId) {
        // 获取会员成长值
        MemberGrowthValueDO growthValue = memberGrowthValueMapper.selectByMemberId(domainId,memberId);
        if (growthValue == null) {
            throw new BusinessException("会员成长值不存在");
        }

        // 获取当前租户和域的所有等级定义，按成长值门槛升序排序
        List<MemberLevelDefinitionDO> levelDefinitions = getLevelDefinitionList(growthValue.getTenantId(), growthValue.getDomainId());

        if (CollUtil.isEmpty(levelDefinitions)) {
            throw new BusinessException("未找到有效的会员等级定义");
        }

        // 计算应该处于的等级
        Integer totalGrowthValue = growthValue.getTotalGrowthValue();
        Integer currentLevelId = growthValue.getCurrentLevelId();
        Integer newLevelId = null;

        for (int i = levelDefinitions.size() - 1; i >= 0; i--) {
            MemberLevelDefinitionDO level = levelDefinitions.get(i);
            if (totalGrowthValue >= level.getGrowthValueThreshold() && level.getStatus() == 0) {
                newLevelId = level.getId();
                break;
            }
        }

        // 如果没有找到合适的等级，使用第一个等级
        if (newLevelId == null) {
            for (MemberLevelDefinitionDO level : levelDefinitions) {
                if (level.getStatus() == 0) {
                    newLevelId = level.getId();
                    break;
                }
            }
        }

        // 如果仍然没有找到合适的等级，抛出异常
        if (newLevelId == null) {
            throw new BusinessException("未找到有效的会员等级定义");
        }

        // 如果等级发生变化，更新会员等级并记录变更历史
        if (!newLevelId.equals(currentLevelId)) {
            // 更新会员成长值表中的当前等级
            growthValue.setCurrentLevelId(newLevelId);

            // 计算下一等级门槛
            Integer nextLevelThreshold = null;
            MemberLevelDefinitionDO currentLevel = null;
            MemberLevelDefinitionDO nextLevel = null;

            for (int i = 0; i < levelDefinitions.size(); i++) {
                if (levelDefinitions.get(i).getId().equals(newLevelId)) {
                    currentLevel = levelDefinitions.get(i);
                    if (i < levelDefinitions.size() - 1) {
                        nextLevel = levelDefinitions.get(i + 1);
                        nextLevelThreshold = nextLevel.getGrowthValueThreshold();
                    }
                    break;
                }
            }

            growthValue.setNextLevelThreshold(nextLevelThreshold);
            memberGrowthValueMapper.updateById(growthValue);

            // 记录等级变更历史
            MemberLevelChangeRecordDO record = new MemberLevelChangeRecordDO();
            record.setTenantId(growthValue.getTenantId());
            record.setDomainId(growthValue.getDomainId());
            record.setMemberId(memberId);
            record.setOldLevelId(currentLevelId);
            record.setNewLevelId(newLevelId);

            // 判断是升级还是降级
            Integer changeType;
            if (currentLevel != null && currentLevelId != null) {
                MemberLevelDefinitionDO oldLevel = memberLevelDefinitionMapper.selectById(currentLevelId);
                if (oldLevel != null) {
                    changeType = currentLevel.getLevelValue() > oldLevel.getLevelValue() ?
                            MemberLevelChangeTypeEnum.UPGRADE.getValue() :
                            MemberLevelChangeTypeEnum.DOWNGRADE.getValue();
                } else {
                    changeType = MemberLevelChangeTypeEnum.INITIALIZE.getValue();
                }
            } else {
                changeType = MemberLevelChangeTypeEnum.INITIALIZE.getValue();
            }

            record.setChangeType(changeType);
            record.setChangeReason("成长值变更自动计算");
            record.setOperator("system");
            memberLevelChangeRecordMapper.insert(record);

            // 发布会员等级变更事件
            MemberLevelChangeEvent event = MemberLevelChangeEvent.builder()
                    .memberId(memberId)
                    .oldLevelId(currentLevelId)
                    .newLevelId(newLevelId)
                    .changeType(changeType)
                    .changeReason(record.getChangeReason())
                    .operator(record.getOperator())
                    .tenantId(growthValue.getTenantId())
                    .domainId(growthValue.getDomainId())
                    .build();
            applicationEventPublisher.publishEvent(event);

            // 清除缓存
            clearMemberCurrentLevelCache(memberId);
        }

        return newLevelId;
    }

    @Override
    public PageResult<MemberLevelChangeRecordVO> getMemberLevelChangeHistory(Integer memberId, Integer pageNum, Integer pageSize) {
        // 分页查询会员等级变更记录
        Page<MemberLevelChangeRecordDO> page = memberLevelChangeRecordMapper.selectPageByMemberId(memberId, pageNum, pageSize);

        // 转换为VO
        List<MemberLevelChangeRecordVO> records = new ArrayList<>();
        for (MemberLevelChangeRecordDO record : page.getRecords()) {
            MemberLevelChangeRecordVO vo = BeanUtil.copyProperties(record, MemberLevelChangeRecordVO.class);

            // 设置旧等级名称
            if (record.getOldLevelId() != null) {
                MemberLevelDefinitionDO oldLevel = memberLevelDefinitionMapper.selectById(record.getOldLevelId());
                if (oldLevel != null) {
                    vo.setOldLevelName(oldLevel.getLevelName());
                }
            }

            // 设置新等级名称
            MemberLevelDefinitionDO newLevel = memberLevelDefinitionMapper.selectById(record.getNewLevelId());
            if (newLevel != null) {
                vo.setNewLevelName(newLevel.getLevelName());
            }

            // 设置变更类型名称
            MemberLevelChangeTypeEnum changeType = MemberLevelChangeTypeEnum.getByValue(record.getChangeType());
            if (changeType != null) {
                vo.setChangeTypeName(changeType.getName());
            }

            records.add(vo);
        }

        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public MemberLevelProgressVO calculateMemberLevelProgress(Integer domainId,Integer memberId) {
        // 获取会员成长值
        MemberGrowthValueDO growthValue = memberGrowthValueMapper.selectByMemberId(domainId,memberId);
        if (growthValue == null) {
            throw new BusinessException("会员成长值不存在");
        }

        // 获取当前等级
        MemberLevelDefinitionDO currentLevel = memberLevelDefinitionMapper.selectById(growthValue.getCurrentLevelId());
        if (currentLevel == null) {
            throw new BusinessException("会员等级不存在");
        }

        // 获取下一等级
        MemberLevelDefinitionDO nextLevel = memberLevelDefinitionMapper.selectNextLevel(
                currentLevel.getDomainId(), currentLevel.getLevelValue());

        // 构建进度信息
        MemberLevelProgressVO progressVO = new MemberLevelProgressVO();
        progressVO.setMemberId(memberId);
        progressVO.setCurrentLevelId(currentLevel.getId());
        progressVO.setCurrentLevelName(currentLevel.getLevelName());
        progressVO.setCurrentLevelValue(currentLevel.getLevelValue());
        progressVO.setCurrentGrowthValue(growthValue.getTotalGrowthValue());
        progressVO.setCurrentLevelThreshold(currentLevel.getGrowthValueThreshold());

        // 设置下一等级信息
        if (nextLevel != null) {
            progressVO.setNextLevelId(nextLevel.getId());
            progressVO.setNextLevelName(nextLevel.getLevelName());
            progressVO.setNextLevelValue(nextLevel.getLevelValue());
            progressVO.setNextLevelThreshold(nextLevel.getGrowthValueThreshold());

            // 计算距离下一等级还需成长值
            Integer growthValueToNextLevel = nextLevel.getGrowthValueThreshold() - growthValue.getTotalGrowthValue();
            progressVO.setGrowthValueToNextLevel(Math.max(growthValueToNextLevel, 0));

            // 计算升级进度百分比
            Integer currentThreshold = currentLevel.getGrowthValueThreshold();
            Integer nextThreshold = nextLevel.getGrowthValueThreshold();
            Integer totalGrowthValue = growthValue.getTotalGrowthValue();

            if (nextThreshold > currentThreshold) {
                Integer progress = (totalGrowthValue - currentThreshold) * 100 / (nextThreshold - currentThreshold);
                progressVO.setUpgradeProgressPercent(Math.min(progress, 100));
            } else {
                progressVO.setUpgradeProgressPercent(100);
            }

            progressVO.setIsHighestLevel(false);
        } else {
            // 已是最高等级
            progressVO.setUpgradeProgressPercent(100);
            progressVO.setIsHighestLevel(true);
        }

        return progressVO;
    }

    /**
     * 清除会员当前等级缓存
     *
     * @param memberId 会员ID
     */
    @CacheEvict(namespace = "MEMBER", key = "'current:' + #memberId")
    public void clearMemberCurrentLevelCache(Integer memberId) {
        // 使用注解自动清除缓存，方法体为空
    }

    /**
     * 清除等级定义列表缓存
     *
     * @param tenantId 租户ID
     * @param domainId 域ID
     */
    @CacheEvict(namespace = "MEMBER", key = "'definition:list:' + #tenantId + ':' + #domainId")
    public void clearLevelDefinitionListCache(Integer tenantId, Integer domainId) {
        // 使用注解自动清除缓存，方法体为空
    }
}

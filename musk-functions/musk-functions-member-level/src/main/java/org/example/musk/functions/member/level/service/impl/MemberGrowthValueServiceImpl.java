package org.example.musk.functions.member.level.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.example.musk.common.context.ThreadLocalTenantContext;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.member.level.constant.MemberLevelConstant;
import org.example.musk.functions.member.level.dao.MemberGrowthValueMapper;
import org.example.musk.functions.member.level.dao.MemberGrowthValueRecordMapper;
import org.example.musk.functions.member.level.dao.MemberLevelDefinitionMapper;
import org.example.musk.functions.member.level.enums.GrowthValueSourceTypeEnum;
import org.example.musk.functions.member.level.enums.PointsChangeTypeEnum;
import org.example.musk.functions.member.level.event.MemberGrowthValueChangeEvent;
import org.example.musk.functions.member.level.model.entity.MemberGrowthValueDO;
import org.example.musk.functions.member.level.model.entity.MemberGrowthValueRecordDO;
import org.example.musk.functions.member.level.model.entity.MemberLevelDefinitionDO;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberGrowthValueVO;
import org.example.musk.functions.member.level.service.MemberGrowthValueService;
import org.example.musk.functions.member.level.service.MemberLevelService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员成长值服务实现类
 *
 * @author musk-functions-member-level
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class MemberGrowthValueServiceImpl implements MemberGrowthValueService {

    @Resource
    private MemberGrowthValueMapper memberGrowthValueMapper;

    @Resource
    private MemberGrowthValueRecordMapper memberGrowthValueRecordMapper;

    @Resource
    private MemberLevelDefinitionMapper memberLevelDefinitionMapper;

    @Resource
    private MemberLevelService memberLevelService;



    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public Integer addGrowthValue(Integer memberId, Integer growthValue, GrowthValueSourceTypeEnum sourceType, String sourceId, String description, String operator) {
        if (growthValue <= 0) {
            throw new BusinessException("成长值必须大于0");
        }

        // 获取会员成长值信息
        MemberGrowthValueDO memberGrowthValue = memberGrowthValueMapper.selectByMemberId( memberId);
        if (memberGrowthValue == null) {
            // 如果不存在，则初始化
            memberGrowthValue = new MemberGrowthValueDO();
            memberGrowthValue.setMemberId(memberId);
            memberGrowthValue.setTotalGrowthValue(0);
            memberGrowthValue.setCurrentPeriodGrowthValue(0);
            memberGrowthValue.setTenantId(ThreadLocalTenantContext.getTenantId());

            // 获取初始等级
            MemberLevelDefinitionDO initialLevel = memberLevelDefinitionMapper.selectByGrowthValue( 0);
            if (initialLevel == null) {
                throw new BusinessException("未找到初始等级");
            }
            memberGrowthValue.setCurrentLevelId(initialLevel.getId());

            // 获取下一等级门槛
            MemberLevelDefinitionDO nextLevel = memberLevelDefinitionMapper.selectNextLevel(
                     initialLevel.getLevelValue());
            if (nextLevel != null) {
                memberGrowthValue.setNextLevelThreshold(nextLevel.getGrowthValueThreshold());
            }

            memberGrowthValueMapper.insert(memberGrowthValue);
        }

        // 记录变更前的值
        Integer beforeValue = memberGrowthValue.getTotalGrowthValue();

        // 更新成长值
        memberGrowthValue.setTotalGrowthValue(memberGrowthValue.getTotalGrowthValue() + growthValue);
        memberGrowthValue.setCurrentPeriodGrowthValue(memberGrowthValue.getCurrentPeriodGrowthValue() + growthValue);
        memberGrowthValueMapper.updateById(memberGrowthValue);

        // 发布成长值变更事件
        MemberGrowthValueChangeEvent event = MemberGrowthValueChangeEvent.builder()
                .memberId(memberId)
                .changeType(PointsChangeTypeEnum.ADD.getValue()) // 1:增加
                .changeValue(growthValue)
                .beforeValue(beforeValue)
                .afterValue(memberGrowthValue.getTotalGrowthValue())
                .sourceType(sourceType.getValue())
                .sourceId(sourceId)
                .description(description)
                .operator(operator)
                .tenantId(memberGrowthValue.getTenantId())
                .domainId(memberGrowthValue.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);

        return memberGrowthValue.getTotalGrowthValue();
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public Integer deductGrowthValue(Integer memberId, Integer growthValue, GrowthValueSourceTypeEnum sourceType, String sourceId, String description, String operator) {
        if (growthValue <= 0) {
            throw new BusinessException("成长值必须大于0");
        }
        // 获取会员成长值信息
        MemberGrowthValueDO memberGrowthValue = memberGrowthValueMapper.selectByMemberId( memberId);
        if (memberGrowthValue == null || memberGrowthValue.getTotalGrowthValue() < growthValue) {
            throw new BusinessException("成长值不足");
        }

        // 记录变更前的值
        Integer beforeValue = memberGrowthValue.getTotalGrowthValue();

        // 更新成长值
        memberGrowthValue.setTotalGrowthValue(memberGrowthValue.getTotalGrowthValue() - growthValue);
        memberGrowthValue.setCurrentPeriodGrowthValue(Math.max(memberGrowthValue.getCurrentPeriodGrowthValue() - growthValue, 0));
        memberGrowthValueMapper.updateById(memberGrowthValue);

        // 发布成长值变更事件
        MemberGrowthValueChangeEvent event = MemberGrowthValueChangeEvent.builder()
                .memberId(memberId)
                .changeType(PointsChangeTypeEnum.DEDUCT.getValue()) // 2:减少
                .changeValue(growthValue)
                .beforeValue(beforeValue)
                .afterValue(memberGrowthValue.getTotalGrowthValue())
                .sourceType(sourceType.getValue())
                .sourceId(sourceId)
                .description(description)
                .operator(operator)
                .tenantId(memberGrowthValue.getTenantId())
                .domainId(memberGrowthValue.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);

        return memberGrowthValue.getTotalGrowthValue();
    }

    @Override
    @Cacheable(namespace = "MEMBER", key = "'growth:' + #memberId", expireSeconds = MemberLevelConstant.MEMBER_GROWTH_VALUE_CACHE_EXPIRE_SECONDS)
    public MemberGrowthValueVO getMemberGrowthValue(Integer memberId) {
        // 从线程上下文获取域ID
        Integer domainId = ThreadLocalTenantContext.getDomainId();

        // 获取会员成长值信息
        MemberGrowthValueDO memberGrowthValue = memberGrowthValueMapper.selectByMemberId( memberId);
        if (memberGrowthValue == null) {
            throw new BusinessException("会员成长值信息不存在");
        }

        // 获取当前等级
        MemberLevelDefinitionDO currentLevel = memberLevelDefinitionMapper.selectById(memberGrowthValue.getCurrentLevelId());
        if (currentLevel == null) {
            throw new BusinessException("会员等级不存在");
        }

        // 获取下一等级
        MemberLevelDefinitionDO nextLevel = null;
        if (memberGrowthValue.getNextLevelThreshold() != null) {
            nextLevel = memberLevelDefinitionMapper.selectNextLevel(currentLevel.getLevelValue());
        }

        // 构建成长值信息
        MemberGrowthValueVO growthValueVO = new MemberGrowthValueVO();
        growthValueVO.setMemberId(memberId);
        growthValueVO.setCurrentLevelId(currentLevel.getId());
        growthValueVO.setCurrentLevelName(currentLevel.getLevelName());
        growthValueVO.setTotalGrowthValue(memberGrowthValue.getTotalGrowthValue());
        growthValueVO.setCurrentPeriodGrowthValue(memberGrowthValue.getCurrentPeriodGrowthValue());
        growthValueVO.setNextLevelThreshold(memberGrowthValue.getNextLevelThreshold());
        growthValueVO.setPeriodStartTime(memberGrowthValue.getPeriodStartTime());
        growthValueVO.setPeriodEndTime(memberGrowthValue.getPeriodEndTime());

        // 设置下一等级信息
        if (nextLevel != null) {
            growthValueVO.setNextLevelId(nextLevel.getId());
            growthValueVO.setNextLevelName(nextLevel.getLevelName());

            // 计算距离下一等级还需成长值
            Integer growthValueToNextLevel = nextLevel.getGrowthValueThreshold() - memberGrowthValue.getTotalGrowthValue();
            growthValueVO.setGrowthValueToNextLevel(Math.max(growthValueToNextLevel, 0));

            // 计算升级进度百分比
            Integer currentThreshold = currentLevel.getGrowthValueThreshold();
            Integer nextThreshold = nextLevel.getGrowthValueThreshold();
            Integer totalGrowthValue = memberGrowthValue.getTotalGrowthValue();

            if (nextThreshold > currentThreshold) {
                Integer progress = (totalGrowthValue - currentThreshold) * 100 / (nextThreshold - currentThreshold);
                growthValueVO.setUpgradeProgressPercent(Math.min(progress, 100));
            } else {
                growthValueVO.setUpgradeProgressPercent(100);
            }
        } else {
            // 已是最高等级
            growthValueVO.setUpgradeProgressPercent(100);
        }

        return growthValueVO;
    }

    @Override
    public PageResult<MemberGrowthValueRecordVO> getMemberGrowthValueHistory(Integer memberId, Integer pageNum, Integer pageSize) {
        // 分页查询成长值变更记录
        Page<MemberGrowthValueRecordDO> page = memberGrowthValueRecordMapper.selectPageByMemberId(memberId, pageNum, pageSize);

        // 转换为VO
        List<MemberGrowthValueRecordVO> records = new ArrayList<>();
        for (MemberGrowthValueRecordDO record : page.getRecords()) {
            MemberGrowthValueRecordVO vo = BeanUtil.copyProperties(record, MemberGrowthValueRecordVO.class);

            vo.setChangeTypeName(PointsChangeTypeEnum.getByValue(record.getChangeType()).getName());

            // 设置来源类型名称
            GrowthValueSourceTypeEnum sourceType = GrowthValueSourceTypeEnum.getByValue(record.getSourceType());
            if (sourceType != null) {
                vo.setSourceTypeName(sourceType.getName());
            }

            records.add(vo);
        }

        return new PageResult<>(records, page.getTotal());
    }

    /**
     * 清除会员成长值缓存
     *
     * @param memberId 会员ID
     */
    @CacheEvict(namespace = "MEMBER", key = "'growth:' + #memberId")
    public void clearMemberGrowthValueCache(Integer memberId) {
        // 使用注解自动清除缓存，方法体为空
    }
}

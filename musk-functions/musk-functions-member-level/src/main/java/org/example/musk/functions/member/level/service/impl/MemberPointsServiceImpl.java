package org.example.musk.functions.member.level.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.common.pojo.db.PageResult;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.member.level.constant.MemberLevelConstant;
import org.example.musk.functions.member.level.dao.MemberPointsMapper;
import org.example.musk.functions.member.level.dao.MemberPointsRecordMapper;
import org.example.musk.functions.member.level.enums.PointsChangeTypeEnum;
import org.example.musk.functions.member.level.enums.PointsSourceTypeEnum;
import org.example.musk.functions.member.level.event.MemberPointsChangeEvent;
import org.example.musk.functions.member.level.model.entity.MemberPointsDO;
import org.example.musk.functions.member.level.model.entity.MemberPointsRecordDO;
import org.example.musk.functions.member.level.model.vo.MemberPointsRecordVO;
import org.example.musk.functions.member.level.model.vo.MemberPointsVO;
import org.example.musk.functions.member.level.service.MemberPointsService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员积分服务实现类
 *
 * @author musk-functions-member-level
 */
@Service
@Slf4j
public class MemberPointsServiceImpl implements MemberPointsService {

    @Resource
    private MemberPointsMapper memberPointsMapper;

    @Resource
    private MemberPointsRecordMapper memberPointsRecordMapper;



    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MEMBER", key = "'points:' + #memberId")
    public Integer addPoints(Integer tenantId,Integer domainId,Integer memberId, Integer points, Integer sourceType, String sourceId, String description, String operator) {
        if (points <= 0) {
            throw new BusinessException("积分值必须大于0");
        }

        // 获取会员积分信息
        MemberPointsDO memberPoints = memberPointsMapper.selectByMemberId(memberId);
        if (memberPoints == null) {
            // 如果不存在，则初始化
            memberPoints = new MemberPointsDO();
            memberPoints.setMemberId(memberId);
            memberPoints.setAvailablePoints(0);
            memberPoints.setFrozenPoints(0);
            memberPoints.setTotalPoints(0);
            memberPoints.setUsedPoints(0);
            memberPoints.setExpiredPoints(0);

            memberPoints.setTenantId(tenantId);
            memberPoints.setDomainId(domainId);

            memberPointsMapper.insert(memberPoints);
        }

        // 记录变更前的值
        Integer beforeValue = memberPoints.getAvailablePoints();

        // 更新积分
        memberPoints.setAvailablePoints(memberPoints.getAvailablePoints() + points);
        memberPoints.setTotalPoints(memberPoints.getTotalPoints() + points);
        memberPointsMapper.updateById(memberPoints);

        // 记录积分变更历史
        MemberPointsRecordDO record = new MemberPointsRecordDO();
        record.setTenantId(memberPoints.getTenantId());
        record.setDomainId(memberPoints.getDomainId());
        record.setMemberId(memberId);
        record.setChangeType(PointsChangeTypeEnum.ADD.getValue());
        record.setChangeValue(points);
        record.setBeforeValue(beforeValue);
        record.setAfterValue(memberPoints.getAvailablePoints());
        record.setSourceType(sourceType);
        record.setSourceId(sourceId);
        record.setDescription(description);
        record.setOperator(operator);
        memberPointsRecordMapper.insert(record);

        // 发布积分变更事件
        MemberPointsChangeEvent event = MemberPointsChangeEvent.builder()
                .memberId(memberId)
                .changeType(record.getChangeType())
                .changeValue(record.getChangeValue())
                .beforeValue(record.getBeforeValue())
                .afterValue(record.getAfterValue())
                .sourceType(record.getSourceType())
                .sourceId(record.getSourceId())
                .description(record.getDescription())
                .operator(record.getOperator())
                .tenantId(memberPoints.getTenantId())
                .domainId(memberPoints.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);



        return memberPoints.getAvailablePoints();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MEMBER", key = "'points:' + #memberId")
    public Integer deductPoints(Integer memberId, Integer points, Integer sourceType, String sourceId, String description, String operator) {
        if (points <= 0) {
            throw new BusinessException("积分值必须大于0");
        }

        // 获取会员积分信息
        MemberPointsDO memberPoints = memberPointsMapper.selectByMemberId(memberId);
        if (memberPoints == null || memberPoints.getAvailablePoints() < points) {
            throw new BusinessException("可用积分不足");
        }

        // 记录变更前的值
        Integer beforeValue = memberPoints.getAvailablePoints();

        // 更新积分
        memberPoints.setAvailablePoints(memberPoints.getAvailablePoints() - points);
        memberPoints.setUsedPoints(memberPoints.getUsedPoints() + points);
        memberPointsMapper.updateById(memberPoints);

        // 记录积分变更历史
        MemberPointsRecordDO record = new MemberPointsRecordDO();
        record.setTenantId(memberPoints.getTenantId());
        record.setDomainId(memberPoints.getDomainId());
        record.setMemberId(memberId);
        record.setChangeType(PointsChangeTypeEnum.DEDUCT.getValue());
        record.setChangeValue(points);
        record.setBeforeValue(beforeValue);
        record.setAfterValue(memberPoints.getAvailablePoints());
        record.setSourceType(sourceType);
        record.setSourceId(sourceId);
        record.setDescription(description);
        record.setOperator(operator);
        memberPointsRecordMapper.insert(record);

        // 发布积分变更事件
        MemberPointsChangeEvent event = MemberPointsChangeEvent.builder()
                .memberId(memberId)
                .changeType(record.getChangeType())
                .changeValue(record.getChangeValue())
                .beforeValue(record.getBeforeValue())
                .afterValue(record.getAfterValue())
                .sourceType(record.getSourceType())
                .sourceId(record.getSourceId())
                .description(record.getDescription())
                .operator(record.getOperator())
                .tenantId(memberPoints.getTenantId())
                .domainId(memberPoints.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);



        return memberPoints.getAvailablePoints();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MEMBER", key = "'points:' + #memberId")
    public Integer freezePoints(Integer memberId, Integer points, Integer sourceType, String sourceId, String description, String operator) {
        if (points <= 0) {
            throw new BusinessException("积分值必须大于0");
        }

        // 获取会员积分信息
        MemberPointsDO memberPoints = memberPointsMapper.selectByMemberId(memberId);
        if (memberPoints == null || memberPoints.getAvailablePoints() < points) {
            throw new BusinessException("可用积分不足");
        }

        // 记录变更前的值
        Integer beforeValue = memberPoints.getAvailablePoints();

        // 更新积分
        memberPoints.setAvailablePoints(memberPoints.getAvailablePoints() - points);
        memberPoints.setFrozenPoints(memberPoints.getFrozenPoints() + points);
        memberPointsMapper.updateById(memberPoints);

        // 记录积分变更历史
        MemberPointsRecordDO record = new MemberPointsRecordDO();
        record.setTenantId(memberPoints.getTenantId());
        record.setDomainId(memberPoints.getDomainId());
        record.setMemberId(memberId);
        record.setChangeType(PointsChangeTypeEnum.FREEZE.getValue());
        record.setChangeValue(points);
        record.setBeforeValue(beforeValue);
        record.setAfterValue(memberPoints.getAvailablePoints());
        record.setSourceType(sourceType);
        record.setSourceId(sourceId);
        record.setDescription(description);
        record.setOperator(operator);
        memberPointsRecordMapper.insert(record);

        // 发布积分变更事件
        MemberPointsChangeEvent event = MemberPointsChangeEvent.builder()
                .memberId(memberId)
                .changeType(record.getChangeType())
                .changeValue(record.getChangeValue())
                .beforeValue(record.getBeforeValue())
                .afterValue(record.getAfterValue())
                .sourceType(record.getSourceType())
                .sourceId(record.getSourceId())
                .description(record.getDescription())
                .operator(record.getOperator())
                .tenantId(memberPoints.getTenantId())
                .domainId(memberPoints.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);



        return memberPoints.getFrozenPoints();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MEMBER", key = "'points:' + #memberId")
    public Integer unfreezePoints(Integer memberId, Integer points, Integer sourceType, String sourceId, String description, String operator) {
        if (points <= 0) {
            throw new BusinessException("积分值必须大于0");
        }

        // 获取会员积分信息
        MemberPointsDO memberPoints = memberPointsMapper.selectByMemberId(memberId);
        if (memberPoints == null || memberPoints.getFrozenPoints() < points) {
            throw new BusinessException("冻结积分不足");
        }

        // 记录变更前的值
        Integer beforeValue = memberPoints.getAvailablePoints();

        // 更新积分
        memberPoints.setAvailablePoints(memberPoints.getAvailablePoints() + points);
        memberPoints.setFrozenPoints(memberPoints.getFrozenPoints() - points);
        memberPointsMapper.updateById(memberPoints);

        // 记录积分变更历史
        MemberPointsRecordDO record = new MemberPointsRecordDO();
        record.setTenantId(memberPoints.getTenantId());
        record.setDomainId(memberPoints.getDomainId());
        record.setMemberId(memberId);
        record.setChangeType(PointsChangeTypeEnum.UNFREEZE.getValue());
        record.setChangeValue(points);
        record.setBeforeValue(beforeValue);
        record.setAfterValue(memberPoints.getAvailablePoints());
        record.setSourceType(sourceType);
        record.setSourceId(sourceId);
        record.setDescription(description);
        record.setOperator(operator);
        memberPointsRecordMapper.insert(record);

        // 发布积分变更事件
        MemberPointsChangeEvent event = MemberPointsChangeEvent.builder()
                .memberId(memberId)
                .changeType(record.getChangeType())
                .changeValue(record.getChangeValue())
                .beforeValue(record.getBeforeValue())
                .afterValue(record.getAfterValue())
                .sourceType(record.getSourceType())
                .sourceId(record.getSourceId())
                .description(record.getDescription())
                .operator(record.getOperator())
                .tenantId(memberPoints.getTenantId())
                .domainId(memberPoints.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);



        return memberPoints.getAvailablePoints();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(namespace = "MEMBER", key = "'points:' + #memberId")
    public Integer expirePoints(Integer memberId, Integer points, Integer sourceType, String sourceId, String description, String operator) {
        if (points <= 0) {
            throw new BusinessException("积分值必须大于0");
        }

        // 获取会员积分信息
        MemberPointsDO memberPoints = memberPointsMapper.selectByMemberId(memberId);
        if (memberPoints == null || memberPoints.getAvailablePoints() < points) {
            throw new BusinessException("可用积分不足");
        }

        // 记录变更前的值
        Integer beforeValue = memberPoints.getAvailablePoints();

        // 更新积分
        memberPoints.setAvailablePoints(memberPoints.getAvailablePoints() - points);
        memberPoints.setExpiredPoints(memberPoints.getExpiredPoints() + points);
        memberPointsMapper.updateById(memberPoints);

        // 记录积分变更历史
        MemberPointsRecordDO record = new MemberPointsRecordDO();
        record.setTenantId(memberPoints.getTenantId());
        record.setDomainId(memberPoints.getDomainId());
        record.setMemberId(memberId);
        record.setChangeType(PointsChangeTypeEnum.EXPIRE.getValue());
        record.setChangeValue(points);
        record.setBeforeValue(beforeValue);
        record.setAfterValue(memberPoints.getAvailablePoints());
        record.setSourceType(sourceType);
        record.setSourceId(sourceId);
        record.setDescription(description);
        record.setOperator(operator);
        memberPointsRecordMapper.insert(record);

        // 发布积分变更事件
        MemberPointsChangeEvent event = MemberPointsChangeEvent.builder()
                .memberId(memberId)
                .changeType(record.getChangeType())
                .changeValue(record.getChangeValue())
                .beforeValue(record.getBeforeValue())
                .afterValue(record.getAfterValue())
                .sourceType(record.getSourceType())
                .sourceId(record.getSourceId())
                .description(record.getDescription())
                .operator(record.getOperator())
                .tenantId(memberPoints.getTenantId())
                .domainId(memberPoints.getDomainId())
                .build();
        applicationEventPublisher.publishEvent(event);



        return memberPoints.getAvailablePoints();
    }

    @Override
    @Cacheable(namespace = "MEMBER", key = "'points:' + #memberId", expireSeconds = MemberLevelConstant.MEMBER_POINTS_CACHE_EXPIRE_SECONDS)
    public MemberPointsVO getMemberPoints(Integer memberId) {
        // 获取会员积分信息
        MemberPointsDO memberPoints = memberPointsMapper.selectByMemberId(memberId);
        if (memberPoints == null) {
            // 如果不存在，则返回默认值
            MemberPointsVO pointsVO = new MemberPointsVO();
            pointsVO.setMemberId(memberId);
            pointsVO.setAvailablePoints(0);
            pointsVO.setFrozenPoints(0);
            pointsVO.setTotalPoints(0);
            pointsVO.setUsedPoints(0);
            pointsVO.setExpiredPoints(0);
            return pointsVO;
        }

        // 转换为VO
        return BeanUtil.copyProperties(memberPoints, MemberPointsVO.class);
    }

    @Override
    public PageResult<MemberPointsRecordVO> getMemberPointsHistory(Integer memberId, Integer pageNum, Integer pageSize) {
        // 分页查询积分变更记录
        Page<MemberPointsRecordDO> page = memberPointsRecordMapper.selectPageByMemberId(memberId, pageNum, pageSize);

        // 转换为VO
        List<MemberPointsRecordVO> records = new ArrayList<>();
        for (MemberPointsRecordDO record : page.getRecords()) {
            MemberPointsRecordVO vo = BeanUtil.copyProperties(record, MemberPointsRecordVO.class);

            // 设置变更类型名称
            PointsChangeTypeEnum changeType = PointsChangeTypeEnum.getByValue(record.getChangeType());
            if (changeType != null) {
                vo.setChangeTypeName(changeType.getName());
            }

            // 设置来源类型名称
            PointsSourceTypeEnum sourceType = PointsSourceTypeEnum.getByValue(record.getSourceType());
            if (sourceType != null) {
                vo.setSourceTypeName(sourceType.getName());
            }

            records.add(vo);
        }

        return new PageResult<>(records, page.getTotal());
    }

    /**
     * 清除会员积分缓存
     *
     * @param memberId 会员ID
     */
    @CacheEvict(namespace = "MEMBER", key = "'points:' + #memberId")
    public void clearMemberPointsCache(Integer memberId) {
        // 使用注解自动清除缓存，方法体为空
    }
}

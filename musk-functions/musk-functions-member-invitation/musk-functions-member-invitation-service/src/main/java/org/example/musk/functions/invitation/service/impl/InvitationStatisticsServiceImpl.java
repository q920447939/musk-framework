package org.example.musk.functions.invitation.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.cache.annotation.CacheEvict;
import org.example.musk.functions.cache.annotation.Cacheable;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationCodeDO;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRelationDO;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRewardRecordDO;
import org.example.musk.functions.invitation.dao.enums.InvitationCodeStatusEnum;
import org.example.musk.functions.invitation.dao.enums.InvitationRewardStatusEnum;
import org.example.musk.functions.invitation.dao.enums.InvitationRewardTypeEnum;
import org.example.musk.functions.invitation.dao.mapper.MemberInvitationCodeMapper;
import org.example.musk.functions.invitation.dao.mapper.MemberInvitationRelationMapper;
import org.example.musk.functions.invitation.dao.mapper.MemberInvitationRewardRecordMapper;
import org.example.musk.functions.invitation.service.InvitationStatisticsService;
import org.example.musk.functions.invitation.service.dto.InvitationStatisticsDTO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 邀请统计服务实现类
 *
 * @author musk-functions-member-invitation
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class InvitationStatisticsServiceImpl implements InvitationStatisticsService {

    @Resource
    private MemberInvitationRelationMapper invitationRelationMapper;

    @Resource
    private MemberInvitationCodeMapper invitationCodeMapper;

    @Resource
    private MemberInvitationRewardRecordMapper rewardRecordMapper;

    @Override
    @Cacheable(namespace = "INVITATION_STATISTICS", key = "'member:' + #memberId")
    public InvitationStatisticsDTO getMemberInvitationStatistics(Integer memberId) {
        if (memberId == null) {
            throw new BusinessException("会员ID不能为空");
        }

        try {
            log.debug("开始计算会员邀请统计，memberId={}", memberId);

            // 1. 统计邀请关系
            Long totalInvitations = invitationRelationMapper.countByInviterMemberId(memberId);

            // 有效邀请（状态为1）
            Long validInvitations = invitationRelationMapper.selectCount(
                    new LambdaQueryWrapper<MemberInvitationRelationDO>()
                            .eq(MemberInvitationRelationDO::getInviterMemberId, memberId)
                            .eq(MemberInvitationRelationDO::getStatus, 1)
            );

            // 今日邀请
            LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            Long todayInvitations = invitationRelationMapper.selectCount(
                    new LambdaQueryWrapper<MemberInvitationRelationDO>()
                            .eq(MemberInvitationRelationDO::getInviterMemberId, memberId)
                            .between(MemberInvitationRelationDO::getRegisterTime, todayStart, todayEnd)
            );

            // 本月邀请
            LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
            LocalDateTime monthEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            Long monthInvitations = invitationRelationMapper.selectCount(
                    new LambdaQueryWrapper<MemberInvitationRelationDO>()
                            .eq(MemberInvitationRelationDO::getInviterMemberId, memberId)
                            .between(MemberInvitationRelationDO::getRegisterTime, monthStart, monthEnd)
            );

            // 2. 计算转化率
            BigDecimal conversionRate = BigDecimal.ZERO;
            if (totalInvitations > 0) {
                conversionRate = BigDecimal.valueOf(validInvitations)
                        .divide(BigDecimal.valueOf(totalInvitations), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            // 3. 统计邀请码
            List<MemberInvitationCodeDO> codes = invitationCodeMapper.selectByInviterMemberId(memberId);
            Long invitationCodeCount = (long) codes.size();
            Long validInvitationCodeCount = codes.stream()
                    .filter(code -> InvitationCodeStatusEnum.VALID.getCode().equals(code.getStatus()))
                    .count();

            // 4. 统计奖励
            List<MemberInvitationRewardRecordDO> rewardRecords = rewardRecordMapper.selectList(
                    new LambdaQueryWrapper<MemberInvitationRewardRecordDO>()
                            .eq(MemberInvitationRewardRecordDO::getRewardMemberId, memberId)
                            .eq(MemberInvitationRewardRecordDO::getStatus, InvitationRewardStatusEnum.GRANTED.getCode())
            );

            Integer totalRewardPoints = rewardRecords.stream()
                    .filter(record -> InvitationRewardTypeEnum.POINTS.getCode().equals(record.getRewardType()))
                    .mapToInt(record -> record.getRewardValue().intValue())
                    .sum();

            Integer totalRewardGrowth = rewardRecords.stream()
                    .filter(record -> InvitationRewardTypeEnum.GROWTH.getCode().equals(record.getRewardType()))
                    .mapToInt(record -> record.getRewardValue().intValue())
                    .sum();

            InvitationStatisticsDTO statistics = InvitationStatisticsDTO.builder()
                    .memberId(memberId)
                    .totalInvitations(totalInvitations)
                    .validInvitations(validInvitations)
                    .todayInvitations(todayInvitations)
                    .monthInvitations(monthInvitations)
                    .conversionRate(conversionRate)
                    .totalRewardPoints(totalRewardPoints)
                    .totalRewardGrowth(totalRewardGrowth)
                    .invitationCodeCount(invitationCodeCount)
                    .validInvitationCodeCount(validInvitationCodeCount)
                    .build();

            log.debug("会员邀请统计计算完成，memberId={}, totalInvitations={}, validInvitations={}",
                    memberId, totalInvitations, validInvitations);

            return statistics;

        } catch (Exception e) {
            log.error("计算会员邀请统计异常，memberId={}", memberId, e);
            throw new BusinessException("计算邀请统计失败：" + e.getMessage());
        }
    }

    @Override
    @Cacheable(namespace = "INVITATION_STATISTICS", key = "'tenant'")
    public InvitationStatisticsDTO getTenantInvitationStatistics() {
        try {
            log.debug("开始计算租户邀请统计");

            // 统计租户下所有邀请关系
            Long totalInvitations = invitationRelationMapper.selectCount(null);

            Long validInvitations = invitationRelationMapper.selectCount(
                    new LambdaQueryWrapper<MemberInvitationRelationDO>()
                            .eq(MemberInvitationRelationDO::getStatus, 1)
            );

            // 今日邀请
            LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            Long todayInvitations = invitationRelationMapper.selectCount(
                    new LambdaQueryWrapper<MemberInvitationRelationDO>()
                            .between(MemberInvitationRelationDO::getRegisterTime, todayStart, todayEnd)
            );

            // 本月邀请
            LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
            LocalDateTime monthEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            Long monthInvitations = invitationRelationMapper.selectCount(
                    new LambdaQueryWrapper<MemberInvitationRelationDO>()
                            .between(MemberInvitationRelationDO::getRegisterTime, monthStart, monthEnd)
            );

            // 计算转化率
            BigDecimal conversionRate = BigDecimal.ZERO;
            if (totalInvitations > 0) {
                conversionRate = BigDecimal.valueOf(validInvitations)
                        .divide(BigDecimal.valueOf(totalInvitations), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            // 统计邀请码
            Long invitationCodeCount = invitationCodeMapper.selectCount(null);
            Long validInvitationCodeCount = invitationCodeMapper.selectCount(
                    new LambdaQueryWrapper<MemberInvitationCodeDO>()
                            .eq(MemberInvitationCodeDO::getStatus, InvitationCodeStatusEnum.VALID.getCode())
            );

            InvitationStatisticsDTO statistics = InvitationStatisticsDTO.builder()
                    .totalInvitations(totalInvitations)
                    .validInvitations(validInvitations)
                    .todayInvitations(todayInvitations)
                    .monthInvitations(monthInvitations)
                    .conversionRate(conversionRate)
                    .invitationCodeCount(invitationCodeCount)
                    .validInvitationCodeCount(validInvitationCodeCount)
                    .build();

            log.debug("租户邀请统计计算完成，totalInvitations={}, validInvitations={}",
                    totalInvitations, validInvitations);

            return statistics;

        } catch (Exception e) {
            log.error("计算租户邀请统计异常", e);
            throw new BusinessException("计算租户邀请统计失败：" + e.getMessage());
        }
    }

    @Override
    @CacheEvict(namespace = "INVITATION_STATISTICS", pattern = "*")
    public void refreshStatisticsCache(Integer memberId) {
        if (memberId != null) {
            log.info("刷新会员邀请统计缓存，memberId={}", memberId);
        } else {
            log.info("刷新所有邀请统计缓存");
        }
    }

}

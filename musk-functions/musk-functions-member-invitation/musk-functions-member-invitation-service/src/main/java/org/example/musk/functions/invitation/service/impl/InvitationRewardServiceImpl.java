package org.example.musk.functions.invitation.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.common.exception.BusinessException;
import org.example.musk.constant.db.DBConstant;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRewardRecordDO;
import org.example.musk.functions.invitation.dao.entity.MemberInvitationRewardRuleDO;
import org.example.musk.functions.invitation.dao.enums.InvitationRewardStatusEnum;
import org.example.musk.functions.invitation.dao.enums.InvitationRewardTargetEnum;
import org.example.musk.functions.invitation.dao.mapper.MemberInvitationRewardRecordMapper;
import org.example.musk.functions.invitation.dao.mapper.MemberInvitationRewardRuleMapper;
import org.example.musk.functions.invitation.service.InvitationRewardService;
import org.example.musk.functions.invitation.service.reward.InvitationRewardProcessor;
import org.example.musk.functions.invitation.service.reward.context.InvitationRewardContext;
import org.example.musk.functions.invitation.service.reward.result.RewardResult;
import org.example.musk.functions.member.invitation.entity.MemberInvitationEvent;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 邀请奖励服务实现类
 *
 * @author musk-functions-member-invitation
 */
@Service
@Slf4j
@DS(DBConstant.MEMBER)
public class InvitationRewardServiceImpl implements InvitationRewardService {

    @Resource
    private MemberInvitationRewardRuleMapper rewardRuleMapper;

    @Resource
    private MemberInvitationRewardRecordMapper rewardRecordMapper;

    @Resource
    private List<InvitationRewardProcessor> rewardProcessors;

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public void processInvitationReward(MemberInvitationEvent event) {
        try {
            log.info("开始处理邀请奖励，eventType={}, inviterMemberId={}, inviteeMemberId={}",
                    event.getEventType(), event.getInviterMemberId(), event.getInviteeMemberId());

            // 查询匹配的奖励规则
            List<MemberInvitationRewardRuleDO> rules = rewardRuleMapper.selectEffectiveRulesByEvent(event.getEventType());
            if (CollUtil.isEmpty(rules)) {
                log.info("未找到匹配的奖励规则，eventType={}", event.getEventType());
                return;
            }

            // 处理每个规则
            for (MemberInvitationRewardRuleDO rule : rules) {
                try {
                    processRewardRule(rule, event);
                } catch (Exception e) {
                    log.error("处理奖励规则异常，ruleId={}, eventType={}", rule.getId(), event.getEventType(), e);
                }
            }

            log.info("邀请奖励处理完成，eventType={}, processedRules={}", event.getEventType(), rules.size());

        } catch (Exception e) {
            log.error("处理邀请奖励异常，eventType={}, inviterMemberId={}, inviteeMemberId={}",
                    event.getEventType(), event.getInviterMemberId(), event.getInviteeMemberId(), e);
            throw new BusinessException("处理邀请奖励失败：" + e.getMessage());
        }
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public boolean grantReward(Long rewardRecordId) {
        if (rewardRecordId == null) {
            throw new BusinessException("奖励记录ID不能为空");
        }

        try {
            // 查询奖励记录
            MemberInvitationRewardRecordDO record = rewardRecordMapper.selectById(rewardRecordId);
            if (record == null) {
                log.warn("奖励记录不存在，rewardRecordId={}", rewardRecordId);
                return false;
            }

            // 检查状态
            if (!InvitationRewardStatusEnum.PENDING.getCode().equals(record.getStatus())) {
                log.warn("奖励记录状态不是待发放，rewardRecordId={}, status={}", rewardRecordId, record.getStatus());
                return false;
            }

            // 查找对应的处理器
            InvitationRewardProcessor processor = findRewardProcessor(record.getRewardType());
            if (processor == null) {
                log.error("未找到奖励处理器，rewardType={}", record.getRewardType());
                updateRewardRecordStatus(rewardRecordId, InvitationRewardStatusEnum.FAILED, "未找到奖励处理器");
                return false;
            }

            // 构建奖励上下文
            InvitationRewardContext context = buildRewardContext(record);

            // 执行奖励发放
            RewardResult result = processor.processReward(context);

            // 更新记录状态
            if (result.isSuccess()) {
                updateRewardRecordStatus(rewardRecordId, InvitationRewardStatusEnum.GRANTED,
                        JSONUtil.toJsonStr(result.getResultData()));
                log.info("奖励发放成功，rewardRecordId={}, rewardType={}, rewardValue={}",
                        rewardRecordId, record.getRewardType(), record.getRewardValue());
                return true;
            } else {
                updateRewardRecordStatus(rewardRecordId, InvitationRewardStatusEnum.FAILED, result.getErrorMessage());
                log.error("奖励发放失败，rewardRecordId={}, error={}", rewardRecordId, result.getErrorMessage());
                return false;
            }

        } catch (Exception e) {
            log.error("发放奖励异常，rewardRecordId={}", rewardRecordId, e);
            updateRewardRecordStatus(rewardRecordId, InvitationRewardStatusEnum.FAILED, "发放异常：" + e.getMessage());
            return false;
        }
    }

    @Override
    public int batchGrantPendingRewards() {
        try {
            List<MemberInvitationRewardRecordDO> pendingRecords = rewardRecordMapper.selectPendingRecords();
            if (CollUtil.isEmpty(pendingRecords)) {
                return 0;
            }

            int successCount = 0;
            for (MemberInvitationRewardRecordDO record : pendingRecords) {
                try {
                    if (grantReward(record.getId())) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.error("批量发放奖励异常，rewardRecordId={}", record.getId(), e);
                }
            }

            log.info("批量发放奖励完成，total={}, success={}", pendingRecords.size(), successCount);
            return successCount;

        } catch (Exception e) {
            log.error("批量发放奖励异常", e);
            return 0;
        }
    }

    @Override
    public int retryFailedRewards() {
        try {
            List<MemberInvitationRewardRecordDO> failedRecords = rewardRecordMapper.selectFailedRecords();
            if (CollUtil.isEmpty(failedRecords)) {
                return 0;
            }

            int successCount = 0;
            for (MemberInvitationRewardRecordDO record : failedRecords) {
                try {
                    // 重置状态为待发放
                    updateRewardRecordStatus(record.getId(), InvitationRewardStatusEnum.PENDING, "重试发放");

                    if (grantReward(record.getId())) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.error("重试发放奖励异常，rewardRecordId={}", record.getId(), e);
                }
            }

            log.info("重试发放奖励完成，total={}, success={}", failedRecords.size(), successCount);
            return successCount;

        } catch (Exception e) {
            log.error("重试发放奖励异常", e);
            return 0;
        }
    }

    @Override
    @DSTransactional(rollbackFor = Exception.class)
    public boolean cancelReward(Long rewardRecordId, String reason) {
        if (rewardRecordId == null) {
            throw new BusinessException("奖励记录ID不能为空");
        }

        return updateRewardRecordStatus(rewardRecordId, InvitationRewardStatusEnum.CANCELLED, reason);
    }

    @Override
    public List<MemberInvitationRewardRecordDO> getMemberRewardRecords(Integer memberId) {
        if (memberId == null) {
            throw new BusinessException("会员ID不能为空");
        }
        return rewardRecordMapper.selectByRewardMemberId(memberId);
    }

    @Override
    public List<MemberInvitationRewardRecordDO> getRewardRecordsByRelation(Long invitationRelationId) {
        if (invitationRelationId == null) {
            throw new BusinessException("邀请关系ID不能为空");
        }
        return rewardRecordMapper.selectByInvitationRelationId(invitationRelationId);
    }

    /**
     * 处理奖励规则
     *
     * @param rule 奖励规则
     * @param event 邀请事件
     */
    private void processRewardRule(MemberInvitationRewardRuleDO rule, MemberInvitationEvent event) {
        // 检查触发条件
        if (!checkTriggerCondition(rule, event)) {
            log.debug("触发条件不满足，跳过规则，ruleId={}", rule.getId());
            return;
        }

        // 根据奖励对象创建奖励记录
        List<Integer> rewardMemberIds = getRewardMemberIds(rule, event);
        for (Integer rewardMemberId : rewardMemberIds) {
            createRewardRecord(rule, event, rewardMemberId);
        }
    }

    /**
     * 检查触发条件
     *
     * @param rule 奖励规则
     * @param event 邀请事件
     * @return 是否满足条件
     */
    private boolean checkTriggerCondition(MemberInvitationRewardRuleDO rule, MemberInvitationEvent event) {
        // 如果没有配置触发条件，默认满足
        if (StrUtil.isBlank(rule.getTriggerCondition())) {
            return true;
        }

        try {
            // 解析触发条件（这里可以实现复杂的条件判断逻辑）
            Map<String, Object> conditions = JSONUtil.toBean(rule.getTriggerCondition(), Map.class);

            // 示例：检查最小消费金额
            if (conditions.containsKey("minAmount") && event.getEventData() != null) {
                BigDecimal minAmount = new BigDecimal(conditions.get("minAmount").toString());
                BigDecimal eventAmount = new BigDecimal(event.getEventData().getOrDefault("amount", "0").toString());
                if (eventAmount.compareTo(minAmount) < 0) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            log.error("检查触发条件异常，ruleId={}, condition={}", rule.getId(), rule.getTriggerCondition(), e);
            return false;
        }
    }

    /**
     * 获取奖励会员ID列表
     *
     * @param rule 奖励规则
     * @param event 邀请事件
     * @return 会员ID列表
     */
    private List<Integer> getRewardMemberIds(MemberInvitationRewardRuleDO rule, MemberInvitationEvent event) {
        List<Integer> memberIds = new ArrayList<>();

        InvitationRewardTargetEnum target = InvitationRewardTargetEnum.fromCode(rule.getRewardTarget());
        if (target == null) {
            return memberIds;
        }

        switch (target) {
            case INVITER:
                if (event.getInviterMemberId() != null) {
                    memberIds.add(event.getInviterMemberId());
                }
                break;
            case INVITEE:
                if (event.getInviteeMemberId() != null) {
                    memberIds.add(event.getInviteeMemberId());
                }
                break;
            case BOTH:
                if (event.getInviterMemberId() != null) {
                    memberIds.add(event.getInviterMemberId());
                }
                if (event.getInviteeMemberId() != null) {
                    memberIds.add(event.getInviteeMemberId());
                }
                break;
        }

        return memberIds;
    }

    /**
     * 创建奖励记录
     *
     * @param rule 奖励规则
     * @param event 邀请事件
     * @param rewardMemberId 奖励会员ID
     */
    private void createRewardRecord(MemberInvitationRewardRuleDO rule, MemberInvitationEvent event, Integer rewardMemberId) {
        try {
            MemberInvitationRewardRecordDO record = MemberInvitationRewardRecordDO.builder()
                    .ruleId(rule.getId())
                    .invitationRelationId(event.getInvitationRelationId())
                    .rewardMemberId(rewardMemberId)
                    .rewardType(rule.getRewardType())
                    .rewardValue(rule.getRewardValue())
                    .triggerEvent(event.getEventType())
                    .triggerSourceId(event.getSourceId())
                    .status(InvitationRewardStatusEnum.PENDING.getCode())
                    .build();

            int result = rewardRecordMapper.insert(record);
            if (result > 0) {
                log.info("创建奖励记录成功，ruleId={}, rewardMemberId={}, rewardType={}, rewardValue={}",
                        rule.getId(), rewardMemberId, rule.getRewardType(), rule.getRewardValue());

                // 如果是立即发放，直接处理
                if (rule.getDelayType() == 1) {
                    grantReward(record.getId());
                }
            }

        } catch (Exception e) {
            log.error("创建奖励记录异常，ruleId={}, rewardMemberId={}", rule.getId(), rewardMemberId, e);
        }
    }

    /**
     * 查找奖励处理器
     *
     * @param rewardType 奖励类型
     * @return 奖励处理器
     */
    private InvitationRewardProcessor findRewardProcessor(String rewardType) {
        return rewardProcessors.stream()
                .filter(processor -> processor.supports(rewardType))
                .findFirst()
                .orElse(null);
    }

    /**
     * 构建奖励上下文
     *
     * @param record 奖励记录
     * @return 奖励上下文
     */
    private InvitationRewardContext buildRewardContext(MemberInvitationRewardRecordDO record) {
        return InvitationRewardContext.builder()
                .rewardRecordId(record.getId())
                .ruleId(record.getRuleId())
                .invitationRelationId(record.getInvitationRelationId())
                .rewardMemberId(record.getRewardMemberId())
                .rewardType(record.getRewardType())
                .rewardValue(record.getRewardValue())
                .triggerEvent(record.getTriggerEvent())
                .triggerSourceId(record.getTriggerSourceId())
                .tenantId(record.getTenantId())
                .domainId(record.getDomainId())
                .build();
    }

    /**
     * 更新奖励记录状态
     *
     * @param rewardRecordId 奖励记录ID
     * @param status 状态
     * @param result 结果
     * @return 是否更新成功
     */
    private boolean updateRewardRecordStatus(Long rewardRecordId, InvitationRewardStatusEnum status, String result) {
        MemberInvitationRewardRecordDO updateEntity = new MemberInvitationRewardRecordDO();
        updateEntity.setId(rewardRecordId);
        updateEntity.setStatus(status.getCode());
        updateEntity.setGrantResult(result);

        if (InvitationRewardStatusEnum.GRANTED.equals(status)) {
            updateEntity.setGrantTime(LocalDateTime.now());
        }

        int updateResult = rewardRecordMapper.updateById(updateEntity);
        return updateResult > 0;
    }

}

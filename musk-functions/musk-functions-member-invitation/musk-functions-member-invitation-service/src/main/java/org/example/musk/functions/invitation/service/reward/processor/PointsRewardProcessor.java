package org.example.musk.functions.invitation.service.reward.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.invitation.dao.enums.InvitationRewardTypeEnum;
import org.example.musk.functions.invitation.service.reward.InvitationRewardProcessor;
import org.example.musk.functions.invitation.service.reward.context.InvitationRewardContext;
import org.example.musk.functions.invitation.service.reward.result.RewardResult;
import org.example.musk.functions.member.level.enums.PointsSourceTypeEnum;
import org.example.musk.functions.member.level.service.MemberPointsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 积分奖励处理器
 *
 * @author musk-functions-member-invitation
 */
@Component
@Slf4j
public class PointsRewardProcessor implements InvitationRewardProcessor {

    @Resource
    private MemberPointsService memberPointsService;

    @Override
    public boolean supports(String rewardType) {
        return InvitationRewardTypeEnum.POINTS.getCode().equals(rewardType);
    }

    @Override
    public RewardResult processReward(InvitationRewardContext context) {
        try {
            log.info("开始处理积分奖励，rewardMemberId={}, rewardValue={}, triggerEvent={}", 
                    context.getRewardMemberId(), context.getRewardValue(), context.getTriggerEvent());

            // 发放积分
            Integer finalPoints = memberPointsService.addPoints(
                    context.getRewardMemberId(),
                    context.getRewardValue().intValue(),
                    PointsSourceTypeEnum.INVITATION,
                    context.getTriggerSourceId(),
                    context.getDescription() != null ? context.getDescription() : "邀请奖励积分",
                    "SYSTEM"
            );

            log.info("积分奖励发放成功，rewardMemberId={}, rewardValue={}, finalPoints={}", 
                    context.getRewardMemberId(), context.getRewardValue(), finalPoints);

            return RewardResult.success(finalPoints);

        } catch (Exception e) {
            log.error("积分奖励发放失败，rewardMemberId={}, rewardValue={}", 
                    context.getRewardMemberId(), context.getRewardValue(), e);
            return RewardResult.failure("积分奖励发放失败：" + e.getMessage());
        }
    }

    @Override
    public String getProcessorName() {
        return "积分奖励处理器";
    }

    @Override
    public int getPriority() {
        return 100;
    }

}

package org.example.musk.functions.invitation.service.reward.processor;

import lombok.extern.slf4j.Slf4j;
import org.example.musk.functions.invitation.dao.enums.InvitationRewardTypeEnum;
import org.example.musk.functions.invitation.service.reward.InvitationRewardProcessor;
import org.example.musk.functions.invitation.service.reward.context.InvitationRewardContext;
import org.example.musk.functions.invitation.service.reward.result.RewardResult;
import org.example.musk.functions.member.level.enums.GrowthValueSourceTypeEnum;
import org.example.musk.functions.member.level.service.MemberGrowthValueService;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 成长值奖励处理器
 *
 * @author musk-functions-member-invitation
 */
@Component
@Slf4j
public class GrowthRewardProcessor implements InvitationRewardProcessor {

    @Resource
    private MemberGrowthValueService memberGrowthValueService;

    @Override
    public boolean supports(String rewardType) {
        return InvitationRewardTypeEnum.GROWTH.getCode().equals(rewardType);
    }

    @Override
    public RewardResult processReward(InvitationRewardContext context) {
        try {
            log.info("开始处理成长值奖励，rewardMemberId={}, rewardValue={}, triggerEvent={}", 
                    context.getRewardMemberId(), context.getRewardValue(), context.getTriggerEvent());

            // 发放成长值
            Integer finalGrowthValue = memberGrowthValueService.addGrowthValue(
                    context.getRewardMemberId(),
                    context.getRewardValue().intValue(),
                    GrowthValueSourceTypeEnum.INVITATION,
                    context.getTriggerSourceId(),
                    context.getDescription() != null ? context.getDescription() : "邀请奖励成长值",
                    "SYSTEM"
            );

            log.info("成长值奖励发放成功，rewardMemberId={}, rewardValue={}, finalGrowthValue={}", 
                    context.getRewardMemberId(), context.getRewardValue(), finalGrowthValue);

            return RewardResult.success(finalGrowthValue);

        } catch (Exception e) {
            log.error("成长值奖励发放失败，rewardMemberId={}, rewardValue={}", 
                    context.getRewardMemberId(), context.getRewardValue(), e);
            return RewardResult.failure("成长值奖励发放失败：" + e.getMessage());
        }
    }

    @Override
    public String getProcessorName() {
        return "成长值奖励处理器";
    }

    @Override
    public int getPriority() {
        return 90;
    }

}

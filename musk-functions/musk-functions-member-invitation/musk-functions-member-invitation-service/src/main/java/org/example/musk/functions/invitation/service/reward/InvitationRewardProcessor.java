package org.example.musk.functions.invitation.service.reward;

import org.example.musk.functions.invitation.service.reward.context.InvitationRewardContext;
import org.example.musk.functions.invitation.service.reward.result.RewardResult;

/**
 * 邀请奖励处理器接口
 *
 * @author musk-functions-member-invitation
 */
public interface InvitationRewardProcessor {

    /**
     * 是否支持该奖励类型
     *
     * @param rewardType 奖励类型
     * @return 是否支持
     */
    boolean supports(String rewardType);

    /**
     * 处理奖励
     *
     * @param context 奖励上下文
     * @return 奖励结果
     */
    RewardResult processReward(InvitationRewardContext context);

    /**
     * 获取处理器名称
     *
     * @return 处理器名称
     */
    String getProcessorName();

    /**
     * 获取优先级
     *
     * @return 优先级（数字越大优先级越高）
     */
    int getPriority();

}

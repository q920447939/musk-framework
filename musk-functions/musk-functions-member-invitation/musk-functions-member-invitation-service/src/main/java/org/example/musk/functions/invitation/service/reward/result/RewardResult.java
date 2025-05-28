package org.example.musk.functions.invitation.service.reward.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 奖励处理结果
 *
 * @author musk-functions-member-invitation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 结果数据
     */
    private Object resultData;

    /**
     * 创建成功结果
     *
     * @return 成功结果
     */
    public static RewardResult success() {
        return RewardResult.builder()
                .success(true)
                .build();
    }

    /**
     * 创建成功结果
     *
     * @param resultData 结果数据
     * @return 成功结果
     */
    public static RewardResult success(Object resultData) {
        return RewardResult.builder()
                .success(true)
                .resultData(resultData)
                .build();
    }

    /**
     * 创建失败结果
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     * @return 失败结果
     */
    public static RewardResult failure(String errorCode, String errorMessage) {
        return RewardResult.builder()
                .success(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    /**
     * 创建失败结果
     *
     * @param errorMessage 错误信息
     * @return 失败结果
     */
    public static RewardResult failure(String errorMessage) {
        return failure("REWARD_FAILED", errorMessage);
    }

}

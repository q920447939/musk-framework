package org.example.musk.auth.vo.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 密码验证结果
 *
 * @author musk
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordValidationResult {

    /**
     * 是否验证通过
     */
    private Boolean valid;

    /**
     * 密码强度等级（1-弱，2-中，3-强）
     */
    private Integer strengthLevel;

    /**
     * 验证失败的原因列表
     */
    private List<String> failureReasons;

    /**
     * 密码强度描述
     */
    private String strengthDescription;

    /**
     * 建议信息
     */
    private List<String> suggestions;

    /**
     * 创建验证成功的结果
     *
     * @param strengthLevel 强度等级
     * @param strengthDescription 强度描述
     * @return 验证结果
     */
    public static PasswordValidationResult success(Integer strengthLevel, String strengthDescription) {
        return PasswordValidationResult.builder()
                .valid(true)
                .strengthLevel(strengthLevel)
                .strengthDescription(strengthDescription)
                .build();
    }

    /**
     * 创建验证失败的结果
     *
     * @param failureReasons 失败原因
     * @param suggestions 建议
     * @return 验证结果
     */
    public static PasswordValidationResult failure(List<String> failureReasons, List<String> suggestions) {
        return PasswordValidationResult.builder()
                .valid(false)
                .strengthLevel(0)
                .failureReasons(failureReasons)
                .suggestions(suggestions)
                .build();
    }
}

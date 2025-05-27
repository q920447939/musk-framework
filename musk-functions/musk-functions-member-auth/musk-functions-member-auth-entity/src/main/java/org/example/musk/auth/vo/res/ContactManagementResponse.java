package org.example.musk.auth.vo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 联系方式管理响应
 *
 * @author musk
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactManagementResponse {

    /**
     * 操作是否成功
     */
    private Boolean success;

    /**
     * 操作类型描述
     */
    private String operationDesc;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 新的联系方式（绑定或更换成功后返回）
     */
    private String newContact;

    /**
     * 是否需要验证（某些操作可能需要额外验证）
     */
    private Boolean needVerification;

    /**
     * 验证提示信息
     */
    private String verificationTip;

    /**
     * 创建成功响应
     *
     * @param operationDesc 操作描述
     * @param newContact 新联系方式
     * @return 响应对象
     */
    public static ContactManagementResponse success(String operationDesc, String newContact) {
        return ContactManagementResponse.builder()
                .success(true)
                .operationDesc(operationDesc)
                .operationTime(LocalDateTime.now())
                .newContact(newContact)
                .needVerification(false)
                .build();
    }

    /**
     * 创建需要验证的响应
     *
     * @param operationDesc 操作描述
     * @param verificationTip 验证提示
     * @return 响应对象
     */
    public static ContactManagementResponse needVerification(String operationDesc, String verificationTip) {
        return ContactManagementResponse.builder()
                .success(false)
                .operationDesc(operationDesc)
                .operationTime(LocalDateTime.now())
                .needVerification(true)
                .verificationTip(verificationTip)
                .build();
    }
}

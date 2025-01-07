package org.example.musk.framework.tenant.service.tenant.bo;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TenantSaveReqBO {

    private Long id;

    @NotNull(message = "租户名不能为空")
    private String name;

    @NotNull(message = "联系人不能为空")
    private String contactName;

    private String contactMobile;

    @NotNull(message = "租户状态")
    private Integer status;

    private String website;

    @NotNull(message = "租户套餐编号不能为空")
    private Long packageId;

    @NotNull(message = "过期时间不能为空")
    private LocalDateTime expireTime;

    @NotNull(message = "账号数量不能为空")
    private Integer accountCount;

    // ========== 仅【创建】时，需要传递的字段 ==========

    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "用户账号由 数字、字母 组成")
    @Size(min = 4, max = 30, message = "用户账号长度为 4-30 个字符")
    private String username;

    private String password;

    @AssertTrue(message = "用户账号、密码不能为空")
    @JsonIgnore
    public boolean isUsernameValid() {
        return id != null // 修改时，不需要传递
                || (ObjectUtil.isAllNotEmpty(username, password)); // 新增时，必须都传递 username、password
    }

}

package org.example.musk.auth.event.login.succ.entity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.example.musk.auth.entity.member.MemberDO;

/**
 * @Description:
 * @date 2024年08月05日
 */
@Data
@Builder
public class LoginSuccEventInfo {
    private MemberDO memberDO;
    private HttpServletRequest request;
    private LoginRequestDTO loginRequestDTO;

}

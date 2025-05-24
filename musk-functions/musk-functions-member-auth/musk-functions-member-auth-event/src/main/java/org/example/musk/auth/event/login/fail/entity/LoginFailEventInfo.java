package org.example.musk.auth.event.login.fail.entity;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Data;
import org.example.musk.auth.event.login.succ.entity.LoginRequestDTO;

/**
 * @Description:
 * @date 2024年08月05日
 */
@Data
@Builder
public class LoginFailEventInfo {
    private HttpServletRequest request;
    private JSONObject failResult;
    private LoginRequestDTO loginRequestDTO;

}

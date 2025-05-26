package org.example.musk.auth.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 注册响应DTO
 *
 * @author musk
 */
@Data
public class RegisterResponseDTO {

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 会员编码
     */
    private String memberCode;

    /**
     * 会员昵称
     */
    private String memberNickName;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private LocalDateTime registerTime;

    /**
     * 是否自动登录
     */
    private boolean autoLogin;

    /**
     * 登录令牌（如果自动登录）
     */
    private String token;

    /**
     * 令牌类型（如果自动登录）
     */
    private String tokenType;

    /**
     * 令牌过期时间（如果自动登录）
     */
    private Long expiresIn;

    /**
     * 注册渠道描述
     */
    private String registerChannel;

    /**
     * 提示信息
     */
    private String message;
}

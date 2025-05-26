package org.example.musk.auth.service.core.helper;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MemberHelper {

    @Value("${musk.auth.default-avatar-url}")
    private String defaultAvatar;

    /**
     * 获取会员默认头像
     * 入参 域名 uri
     */
    public  String getDefaultAvatar( ) {
        return defaultAvatar;
    }

}

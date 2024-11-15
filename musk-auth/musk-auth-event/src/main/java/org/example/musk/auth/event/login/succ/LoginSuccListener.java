package org.example.musk.auth.event.login.succ;


import jakarta.annotation.Resource;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.event.login.InterfaceImplementationSorter;
import org.example.musk.auth.event.login.succ.entity.LoginSuccEventInfo;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 登录成功后的事件
 *
 * @author
 * @date 2024/07/02
 */
@Slf4j
@Component
@Data
public class LoginSuccListener {

    @Resource
    private InterfaceImplementationSorter interfaceImplementationSorter;

    /**
     * 记录登录日志
     */
    @EventListener
    @Async
    public void recordLoginLog(@NonNull LoginSuccEventInfo loginSuccEventInfo) {
        List<LoginSuccEventIntl> loginSuccEventIntlList = interfaceImplementationSorter.getSortedImplementations(LoginSuccEventIntl.class);
        for (LoginSuccEventIntl loginSuccEventIntl : loginSuccEventIntlList) {
            loginSuccEventIntl.event(loginSuccEventInfo);
        }
    }
}

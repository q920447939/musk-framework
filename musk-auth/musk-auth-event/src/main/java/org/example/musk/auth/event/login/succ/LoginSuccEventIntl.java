package org.example.musk.auth.event.login.succ;

import org.example.musk.auth.event.login.succ.entity.LoginSuccEventInfo;

/**
 * ClassName: 登录成功事件
 */
public interface LoginSuccEventIntl {

    void event(LoginSuccEventInfo loginSuccEventInfo);
}

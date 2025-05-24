package org.example.musk.auth.event.login.fail;

import org.example.musk.auth.event.login.succ.entity.LoginSuccEventInfo;

/**
 * ClassName: 登录失败事件
 */
public interface LoginFailEventIntl {

    void event(LoginSuccEventInfo loginSuccEventInfo);
}

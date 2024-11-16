package org.example.musk.auth.event.login.succ.impl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.dromara.hutool.http.server.servlet.JakartaServletUtil;
import org.example.musk.auth.enums.loginLog.LoginLogStatusEnum;
import org.example.musk.auth.entity.loginlog.MemberLoginLogDO;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.auth.event.login.succ.LoginSuccEventIntl;
import org.example.musk.auth.event.login.succ.entity.LoginSuccEventInfo;
import org.example.musk.auth.service.core.log.login.MemberLoginLogService;
import org.springframework.stereotype.Service;


@Service
public class RecordLoginLog implements LoginSuccEventIntl {

    @Resource
    private MemberLoginLogService memberLoginLogService;

    @Override
    public void event(LoginSuccEventInfo loginSuccEventInfo) {
        MemberDO memberDO = loginSuccEventInfo.getMemberDO();
        MemberLoginLogDO info = new MemberLoginLogDO();
        info.setMemberId(memberDO.getId());
        info.setMemberCode(memberDO.getMemberCode());
        info.setLoginStatus((short) LoginLogStatusEnum.SUCC.getStatus().intValue());
        info.setLoginParams(JSONObject.toJSONString(loginSuccEventInfo.getLoginRequestDTO()));
        //info.setFailReason();
        //info.setTraceId();
        info.setRequestIp(JakartaServletUtil.getClientIP(loginSuccEventInfo.getRequest()));
        info.setUserAgent(JakartaServletUtil.getHeaderIgnoreCase(loginSuccEventInfo.getRequest(), "User-Agent"));
        memberLoginLogService.saveMember(info);
    }
}

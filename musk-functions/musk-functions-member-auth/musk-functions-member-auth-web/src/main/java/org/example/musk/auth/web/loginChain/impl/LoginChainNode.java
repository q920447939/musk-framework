package org.example.musk.auth.web.loginChain.impl;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.musk.auth.service.core.member.MemberService;
import org.example.musk.auth.web.loginChain.AbstractLoginChain;
import org.example.musk.auth.web.loginChain.entity.LoginChainRequest;
import org.example.musk.auth.web.loginChain.entity.LoginChainResult;
import org.example.musk.auth.entity.member.MemberDO;
import org.example.musk.common.exception.BusinessPageExceptionEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


/**
 * 登录链节点
 * ClassName:
 *
 * @author
 * @Description:
 * @date 2023年07月10日
 */
@Service
@Order(40)
@Slf4j
@Scope("prototype")
public class LoginChainNode extends AbstractLoginChain {

    /**
     * 用户服务
     */
    @Resource
    private MemberService memberService;


    /**
     * 处理
     *
     * @param loginChainRequest 登录请求链
     * @return {@link LoginChainResult}
     */
    @SneakyThrows
    @Override
    protected LoginChainResult handle(LoginChainRequest loginChainRequest) {
        String userName = loginChainRequest.getUsername();
        String password = loginChainRequest.getPassword();
        MemberDO user = null;
        user = memberService.login(userName, password);
        if (null == user) {  // 验证失败
            return LoginChainResult.fail(BusinessPageExceptionEnum.API_INVALID_USERNAME_CODE);
        }
        return LoginChainResult.succWithData(user);
    }


}

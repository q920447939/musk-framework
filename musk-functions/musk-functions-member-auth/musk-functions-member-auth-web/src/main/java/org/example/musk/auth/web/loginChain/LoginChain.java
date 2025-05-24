package org.example.musk.auth.web.loginChain;


import org.example.musk.auth.web.loginChain.entity.ChainContext;
import org.example.musk.auth.web.loginChain.entity.LoginChainRequest;
import org.example.musk.auth.web.loginChain.entity.LoginChainResult;

/**
 * 登录链
 * ClassName: LoginChain
 *
 * @author
 * @Description:
 * @date 2023年07月10日
 */
public interface LoginChain {
    /**
     * 是否匹配规则
     * 默认匹配
     * @return boolean
     */
    default boolean match (){
        return true;
    };

    /**
     * 身份验证
     *
     * @param loginChainRequest 登录请求链
     * @param chainContext      链上下文
     * @return {@link LoginChainResult}
     */
    LoginChainResult auth(LoginChainRequest loginChainRequest, ChainContext chainContext);


}

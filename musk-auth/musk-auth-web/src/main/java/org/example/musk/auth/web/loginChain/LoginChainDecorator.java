/**
 * @Project:
 * @Author:
 * @Date: 2023年07月10日
 */
package org.example.musk.auth.web.loginChain;

import jakarta.annotation.Resource;
import org.example.musk.auth.web.loginChain.entity.ChainContext;
import org.example.musk.auth.web.loginChain.entity.LoginChainRequest;
import org.example.musk.auth.web.loginChain.entity.LoginChainResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录链装饰
 * ClassName: LoginChainDecorator
 *
 * @author
 * @Description:
 * @date 2023年07月10日
 */
@Service
public class LoginChainDecorator {
    @Resource
    private List<LoginChain> nodeList;

    /**
     * 身份验证
     *
     * @param loginChainRequest 登录请求链
     * @return {@link LoginChainResult}
     */
    public final LoginChainResult auth(LoginChainRequest loginChainRequest) {
        ChainContext chainContext = new ChainContext();
        LoginChainResult r = null;
        for (LoginChain node : nodeList) {
            if (!node.match()) {
                continue;
            }
            LoginChainResult result = node.auth(loginChainRequest, chainContext);
            if (!result.isSuccess()){
                return result;
            }
            r = result;
        }
        return r;
    }
}

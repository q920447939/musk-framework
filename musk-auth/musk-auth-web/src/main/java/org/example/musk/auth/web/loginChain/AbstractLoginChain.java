/**
 * @Project:
 * @Author:
 * @Date: 2023年07月10日
 */
package org.example.musk.auth.web.loginChain;

import cn.hutool.core.lang.Assert;
import org.example.musk.auth.web.loginChain.entity.ChainContext;
import org.example.musk.auth.web.loginChain.entity.LoginChainRequest;
import org.example.musk.auth.web.loginChain.entity.LoginChainResult;

/**
 * 抽象登录链
 * ClassName: AbstractLoginChain
 *  1.执行前置处理
 *  2。执行处理类
 *  3.执行后置处理
 *      3.1 如果2的结果是成功  那么执行 后置处理成功方法
 *      3.2 如果2的结果是失败  那么执行 后置处理失败方法
 * @author
 * @Description:
 * @date 2023年07月10日
 */
public abstract  class AbstractLoginChain implements LoginChain{

    /**
     * 链上下文
     */
    protected ChainContext chainContext;


    /**
     * 身份验证
     *
     * @param loginChainRequest 登录请求链
     * @param chainContext      链上下文
     * @return {@link LoginChainResult}
     */
    @Override
    public final LoginChainResult auth(LoginChainRequest loginChainRequest, ChainContext chainContext) {
        this.chainContext = chainContext;
        LoginChainResult preHandleResult = preHandle(loginChainRequest);
        if (null != preHandleResult && !preHandleResult.isSuccess()) {
            return preHandleResult;
        }
        LoginChainResult result = this.handle(loginChainRequest);
        Assert.notNull(result,()->{
            throw new RuntimeException("处理handle时,result不能为空！");
        });
        afterHandle(loginChainRequest,result);
        if (!result.isSuccess()) {
            return afterFailHandle(loginChainRequest,result);
        }
        afterSuccHandle(loginChainRequest,result);
        return result;
    }

    /**
     * 前处理
     *
     * @param loginChainRequest 登录请求链
     * @return {@link LoginChainResult}
     */
    protected LoginChainResult preHandle(LoginChainRequest loginChainRequest) {
        return null;
    }

    /**
     * 处理
     *
     * @param loginChainRequest 登录请求链
     * @return {@link LoginChainResult}
     */
    protected abstract LoginChainResult handle(LoginChainRequest loginChainRequest) ;

    /**
     * 处理成功后执行
     *
     * @param loginChainRequest 登录请求链
     * @param result            结果
     */
    protected void afterSuccHandle(LoginChainRequest loginChainRequest,LoginChainResult result ) {
        return ;
    }

    /**
     * 失败处理后后执行
     *
     * @param loginChainRequest 登录请求链
     * @param result            结果
     */
    protected LoginChainResult afterFailHandle(LoginChainRequest loginChainRequest,LoginChainResult result ) {
        return result;
    }

    /**
     * 处理后执行
     *
     * @param loginChainRequest 登录请求链
     * @param result            结果
     */
    protected void afterHandle(LoginChainRequest loginChainRequest,LoginChainResult result ) {
        return ;
    }




}

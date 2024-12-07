/**
 * @Project:
 * @Author:
 * @Date: 2021年08月23日
 */
package org.example.musk.auth.web.interceptor.decorator;


import jakarta.servlet.http.HttpServletRequest;

/**
 * ClassName: LoginDecorator
 *
 * @author
 * @Description:
 * @date 2021年08月23日
 */
public interface FilterAuthDecorator {
    boolean match(HttpServletRequest httpServletRequest, Object object);
}

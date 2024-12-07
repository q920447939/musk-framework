/**
 * @Project:
 * @Author:
 * @Date: 2021年08月23日
 */
package org.example.musk.auth.web.interceptor.decorator.authFilter;


import jakarta.servlet.http.HttpServletRequest;

/**
 * ClassName: ThridPartyLogin
 *
 * @author
 * @Description:
 * @date 2021年08月23日
 */
public interface FilterAuth {
    boolean match(HttpServletRequest httpServletRequest, Object object);


}

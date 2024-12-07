/**
 * @Project:
 * @Author:
 * @Date: 2021年08月23日
 */
package org.example.musk.auth.web.interceptor.decorator;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.musk.auth.web.interceptor.decorator.authFilter.FilterAuth;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * ClassName: ThirdPartyLoginDecoratorImpl
 *
 * @author
 * @Description:
 * @date 2021年08月23日
 */
@Service
public class FilterAuthDecoratorImpl implements FilterAuthDecorator {

    @Resource
    private List<FilterAuth> filterAuths;

    @Override
    public boolean match(HttpServletRequest httpServletRequest, Object object) {
        for (FilterAuth auth : filterAuths) {
            boolean match = auth.match(httpServletRequest, object);
            if (match) return true;
        }
        return false;
    }


}

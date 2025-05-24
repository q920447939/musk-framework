package org.example.musk.auth.web.interceptor.decorator.authFilter.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.example.musk.auth.web.interceptor.decorator.authFilter.FilterAuth;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateTokenFilterAuthImpl implements FilterAuth {

    @Override
    public boolean match(HttpServletRequest httpServletRequest, Object object) {
        return false;
    }
}

package com.somemore.global.auth.annotation;

import com.somemore.global.auth.authentication.UserIdentity;
import com.somemore.global.exception.InvalidAuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.somemore.global.exception.ExceptionMessage.AUTHENTICATION_MISSING;
import static com.somemore.global.exception.ExceptionMessage.INVALID_PRINCIPAL_TYPE;

@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new InvalidAuthenticationException(AUTHENTICATION_MISSING);
        }

        if (authentication.getPrincipal() instanceof UserIdentity principal) {
            return principal.userId();
        }

        return new InvalidAuthenticationException(INVALID_PRINCIPAL_TYPE);
    }
}
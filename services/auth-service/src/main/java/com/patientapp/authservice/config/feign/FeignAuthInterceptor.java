package com.patientapp.authservice.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {
    private static final String ACCESS_TOKEN_COOKIE = "access_token";

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes instanceof ServletRequestAttributes servletAttributes) {
            HttpServletRequest request = servletAttributes.getRequest();

            if (request.getCookies() != null) {
                Arrays.stream(request.getCookies())
                        .filter(cookie -> ACCESS_TOKEN_COOKIE.equals(cookie.getName()))
                        .findFirst()
                        .ifPresent(cookie -> template.header("Authorization", "Bearer " + cookie.getValue()));
            }
        }
    }
}

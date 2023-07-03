package com.example.slotweb.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath("/login/sign-in")
                .queryParam("message", e.getMessage());

        httpServletResponse.sendRedirect(builder.build().encode().toUriString());
    }

}

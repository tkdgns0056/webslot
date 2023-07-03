package com.example.slotweb.utils;

import com.example.slotweb.security.LoginMember;
import com.example.slotweb.service.member.dto.Member;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class LoginUtils {


    public static Member getMember(){
        HttpSession httpSession = WebUtils.getRequest().getSession(true);
        SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext == null) return null;
        return ((LoginMember) securityContext.getAuthentication().getPrincipal()).getMember();
    }

    public static boolean isLogin() {
        return getMember() != null;
    }

    public static String getMemberId() {
        Member member = getMember();
        if( member == null) return null;
        return member.getMemberId();
    }

}
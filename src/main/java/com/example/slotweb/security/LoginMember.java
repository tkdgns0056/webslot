package com.example.slotweb.security;

import com.example.slotweb.service.member.dto.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Arrays;

@Getter @Setter
public class LoginMember extends User implements Serializable {

    private static final long serialVersionUID = 3577536960281439323L;

    private Member member;

    public LoginMember(Member member) {
        super(member.getMemberId(), member.getPassword(), Arrays.asList(new SimpleGrantedAuthority(member.getRoleId())));
        this.member = member;
    }
}

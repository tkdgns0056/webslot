package com.example.slotweb.config;

import com.example.slotweb.security.CustomAuthenticationProvider;
import com.example.slotweb.security.LoginFailHandler;
import com.example.slotweb.security.LoginSuccessHandler;
import com.example.slotweb.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@RequiredArgsConstructor
@EnableWebSecurity // 1
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig { // 2

    private final CustomAuthenticationProvider authProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(
                        "/resources/**"
                );
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authProvider));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests() // 6
                     .antMatchers("/login/sign-in").permitAll() // 누구나 접근 허용
//                    .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
//                    .antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
                    .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
                .and()
                    .formLogin() // 7
                        .loginPage("/login/sign-in") // 로그인 페이지 링크
                        .loginProcessingUrl("/login/process")
                        .failureHandler(loginFailHandler())
                        .successHandler(loginSuccessHandler())
                .and()
                    .logout() // 8
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login/sign-in") // 로그아웃 성공시 리다이렉트 주소
                        .invalidateHttpSession(true);

        return httpSecurity.build();
    }

    @Bean
    public LoginFailHandler loginFailHandler() {
        return new LoginFailHandler();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

}
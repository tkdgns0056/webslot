package com.example.slotweb.page.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login/sign-in")
    public String viewLogin(){
        return "login/sign-in";
    }
}

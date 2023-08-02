package com.web.billim.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordConfig {

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

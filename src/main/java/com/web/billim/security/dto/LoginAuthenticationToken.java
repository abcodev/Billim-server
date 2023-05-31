package com.web.billim.security.dto;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoginAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final String password;


    // 로그인 filter -> provider , 인증 전
    public LoginAuthenticationToken(String email, String password){
        super(null);
        this.email = email;
        this.password = password;
        setAuthenticated(false);
    }


    // provider -> filter 인증 후 / true
    public LoginAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String email) {
        super(authorities);
        this.email = email;
        this.password = null;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }
}

package com.web.billim.jwt.dto;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Object email;
    private final Object credentials;


    // url 요청시 token 넣어주기 / 인증전
    public JwtAuthenticationToken(Object  credentials){
        super(null);
        this.email = null;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    // provider -> filter 반환
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object email){
        super(authorities);
        this.email = email;
        this.credentials = null;
        setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }

//    @Override
//    public boolean implies(Subject subject) {
//        return super.implies(subject);
//    }

}

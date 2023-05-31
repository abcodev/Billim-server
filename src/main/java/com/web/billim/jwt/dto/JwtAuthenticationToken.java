package com.web.billim.jwt.dto;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final String credentials;


    // url 요청시 token 넣어주기 / 인증전
    public JwtAuthenticationToken(String  credentials){
        super(null);
        this.email = null;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    // provider -> filter 반환

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String email){
        super(authorities);
        this.email = email;
        this.credentials = null;
        setAuthenticated(true);
    }


    @Override
    public String getCredentials() {
        return this.credentials;
    }

    @Override
    public String getPrincipal() {
        return this.email;
    }

//    @Override
//    public boolean implies(Subject subject) {
//        return super.implies(subject);
//    }
}

package com.web.billim.security.jwt.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class BillimAuthentication extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;


    // 인증전 -- token 넣어주기 / false
    public BillimAuthentication(Object credentials){
        super(null);
        this.principal = null;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public BillimAuthentication(Object principal, Object credentials){
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }


    // 인증 후 / true
    public BillimAuthentication(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

//    @Override
//    public boolean implies(Subject subject) {
//        return super.implies(subject);
//    }
}

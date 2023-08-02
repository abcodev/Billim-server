package com.web.billim.security.oauth;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Data
public class OauthMember implements  OAuth2User {

    private final String email;
    private final String nickName;

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

package com.web.billim.security.oauth;

import java.util.Map;

public class KakaoLogin implements OAuthLogin{

    private Map<String, Object> kakaoAttributes;

    public KakaoLogin(Map<String, Object> kakaoAttributes) {
        this.kakaoAttributes = kakaoAttributes;
    }

    @Override
    public long getProviderId() {
        return 0;
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}

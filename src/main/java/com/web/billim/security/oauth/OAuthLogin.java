package com.web.billim.security.oauth;

public interface OAuthLogin {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getImageUrl();
}

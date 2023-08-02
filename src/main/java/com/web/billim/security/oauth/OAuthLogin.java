package com.web.billim.security.oauth;

public interface OAuthLogin {
    long getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getImageUrl();
}

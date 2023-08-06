package com.web.billim.oauth.dto;

public interface OAuthLogin {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getImageUrl();
}

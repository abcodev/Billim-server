package com.web.billim.oauth.dto;

import java.time.LocalDateTime;

public interface OAuthLogin {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getImageUrl();
    String getRefreshToken();
    LocalDateTime getRefreshTokenExpiredAt();
}

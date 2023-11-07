package com.web.billim.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
@AllArgsConstructor
@Builder
public class KakaoLogin implements OAuthLogin {

    private String accountId;
    private String email;
    private String nickname;
    private String imageUrl;
    private String refreshToken;
    private long refreshTokenExpiresIn;

    public static KakaoLogin ofKaKao(OAuth2UserRequest oauthRequest, Map<String, Object> kakaoAttributes){
        Map<String,Object> kakaoAccount = (Map<String, Object>) kakaoAttributes.get("kakao_account");
        Map<String,Object> properties = (Map<String, Object>) kakaoAttributes.get("properties");
        return KakaoLogin.builder()
                .accountId(String.valueOf(kakaoAttributes.get("id")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .nickname(String.valueOf(properties.get("nickname")))
                .imageUrl(String.valueOf(properties.get("profile_image")))
                .refreshToken(String.valueOf(oauthRequest.getAdditionalParameters().get("refresh_token")))
                .refreshTokenExpiresIn((Long) oauthRequest.getAdditionalParameters().get("refresh_token_expires_in"))
                .build();
    }

    @Override
    public String getProviderId() {
        return this.accountId;
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.nickname;
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public String getRefreshToken() {
        return this.refreshToken;
    }

    @Override
    public LocalDateTime getRefreshTokenExpiredAt() {
        return LocalDateTime.now()
                .plus(Duration.of(this.refreshTokenExpiresIn, ChronoUnit.SECONDS));
    }
}

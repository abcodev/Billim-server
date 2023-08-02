package com.web.billim.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;
@AllArgsConstructor
@Builder
public class KakaoLogin implements OAuthLogin{

    private String accountId;
    private String email;
    private String nickname;

    private String imageUrl;


    public static KakaoLogin ofKaKao(Map<String, Object> kakaoAttributes){
        Map<String,Object> kakaoAccount = (Map<String, Object>) kakaoAttributes.get("kakao_account");
        Map<String,Object> properties = (Map<String, Object>) kakaoAttributes.get("properties");
        return KakaoLogin.builder()
                .accountId(String.valueOf(kakaoAttributes.get("id")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .nickname(String.valueOf(properties.get("nickname")))
                .imageUrl(String.valueOf(properties.get("profile_image")))
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
}

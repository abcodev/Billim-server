package com.web.billim.security.oauth;

import java.util.Map;

public class KakaoLogin {

    private Map<String, Object> kakaoAttributes;

    public KakaoLogin(Map<String, Object> kakaoAttributes) {
        this.kakaoAttributes = kakaoAttributes;
    }


//    private long oauthId;
//    private String providerName;
//    private String accountId;
//    private String email;
//    private String nickName;
//    public KakaoLogin(Map<String,Object> attributes) {
//        this.oauthId = (long) attributes.get("id");
//        this.providerName = "kakao";
//        this.email = attributes.get("email").toString();
//        this.nickName = attributes.get("nickname").toString();
//    }


}

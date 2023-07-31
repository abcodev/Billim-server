package com.web.billim.security.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OauthService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest : " + userRequest);
        // org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest@200b7145
        log.info("ClientRegistration : "+userRequest.getClientRegistration()); // ClientRegistration 정보
		log.info("AccessToken : "+userRequest.getAccessToken().getTokenValue()); // accessToken 가져오기

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return oAuth2UserLogin(userRequest,oAuth2User);
    }

    private OAuth2User oAuth2UserLogin(OAuth2UserRequest userRequest, OAuth2User oAuth2User){
        // SNS TYPE
        String provider = userRequest.getClientRegistration().getClientName();

        if(provider.equals("KAKAO")){
            KakaoLogin kakaoLogin = new KakaoLogin(oAuth2User.getAttributes());
        }
        return new OauthMember("test1","닉네임");
    }
}

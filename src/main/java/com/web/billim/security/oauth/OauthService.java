package com.web.billim.security.oauth;

import com.web.billim.exception.DuplicatedException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService extends DefaultOAuth2UserService {

    private final MemberService memberService;


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

        OAuthLogin kakaoLogin = null;
        if(provider.equals("KAKAO")){
            kakaoLogin = KakaoLogin.ofKaKao(oAuth2User.getAttributes());
            log.info(oAuth2User.getAttributes().toString());
        }

        log.info("KAKAO 중복 이메일 체크");
        if(memberService.existByEmail(kakaoLogin.getEmail())){
            log.info("사용중인 이메일이 있음");
            throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
        }
        memberService.register(kakaoLogin);
        log.info("KAKAO 사용자 Member 저장");

        return new OauthMember("test1","닉네임");
    }
}

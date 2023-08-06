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
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest : " + userRequest);
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
        }
        if(memberService.existByEmail(Objects.requireNonNull(kakaoLogin).getEmail())){
            throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
        }
        Member member = memberService.register(kakaoLogin);


        return new OauthMember(kakaoLogin,member);
    }
}

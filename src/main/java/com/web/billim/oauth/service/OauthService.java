package com.web.billim.oauth.service;

import com.web.billim.member.domain.Member;
import com.web.billim.member.service.MemberService;
import com.web.billim.oauth.domain.SocialMember;
import com.web.billim.oauth.dto.KakaoLogin;
import com.web.billim.oauth.dto.OAuthLogin;
import com.web.billim.oauth.dto.OauthMember;
import com.web.billim.oauth.repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService extends DefaultOAuth2UserService {

    private final MemberService memberService;
    private final OAuthRepository oAuthRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest : " + userRequest);
        log.info("ClientRegistration : " + userRequest.getClientRegistration()); // ClientRegistration 정보
        log.info("AccessToken : " + userRequest.getAccessToken().getTokenValue()); // accessToken 가져오기
        log.info("login");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        return oAuth2UserLogin(userRequest, oAuth2User);
    }

    private OAuth2User oAuth2UserLogin(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        // SNS TYPE
        String provider = userRequest.getClientRegistration().getClientName();
        OAuthLogin oAuthLogin = null;
        if (provider.equals("KAKAO")) {
            oAuthLogin = KakaoLogin.ofKaKao(oAuth2User.getAttributes());
            log.info("카카오 소셜 사용자");
        }

        SocialMember socialMember;
        if(existByAccountId(oAuthLogin.getProviderId())) {
            log.info("기존 카카오톡 로그인 회원");
            socialMember = oAuthRepository.findByAccountId(oAuthLogin.getProviderId());
        }else {
            log.info("신규 카카오톡 로그인 회원");
            Member member = memberService.register(oAuthLogin);  // member 티이블에 저장 -> 신규
            socialMember = SocialMember.of(member,oAuthLogin); // 객체 만들기 --> 공통
            save(socialMember); // 소셜 테이블에 저장 -> 신규 로그인
        }
        return new OauthMember(socialMember);
    }

    public SocialMember save(SocialMember socialMember) {
        return oAuthRepository.save(socialMember);
    }

    public Boolean existByAccountId(String accountId){
        return oAuthRepository.existsByAccountId(accountId);
    }

}

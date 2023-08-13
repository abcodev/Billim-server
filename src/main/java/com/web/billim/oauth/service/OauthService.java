package com.web.billim.oauth.service;

import com.web.billim.exception.DuplicatedException;
import com.web.billim.exception.handler.ErrorCode;
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

import java.util.Objects;

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
        }

        // email 이 존재하는지 확인
//        if(memberService.existByEmail(Objects.requireNonNull(oAuthLogin).getEmail())){
//            throw new DuplicatedException(ErrorCode.DUPLICATE_EMAIL);
//        }

        // 없다면 member 등록
        Member member = memberService.register(oAuthLogin);

        // 소셜 테이블에도 저장
        SocialMember socialMember = SocialMember.of(member,oAuthLogin);
        save(socialMember);
        log.info("social 회원");
        return new OauthMember(oAuthLogin,member);

    }

    public SocialMember save(SocialMember socialMember) {
        return oAuthRepository.save(socialMember);
    }


}

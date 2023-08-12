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

//        // 1. 일반 회원가입을 했어 slolee@naver.com 이걸로
//        // 2. 카카오 로그인을 눌렀어
//        //     2-1. 만약 로그인 화면에서 카카오 로그인을 누른거면 에러를 내고,
//        //     2-2. 마이페이지에서 카카오 연동(로그인)을 누른거면 연동처리를 하고,
//        // 이걸 분기를 태우고 싶으면 Spring Security OAuth2 를 버리고 직접 연동을 해야한다.
//        Member member = null;
//        // email 이 존재하는지 확인
//        if (memberService.existByEmail(Objects.requireNonNull(oAuthLogin).getEmail())) {
//            // TODO : 로그인 처리를 해야지 에러를 낼게아니다.
//            // TODO : 해당 member 가 SocialMember 테이블에도 존재하는지 확인하고,
//            //     -> 존재안해? 에러
//            //     -> 존재하네? 그러면 로그인 진행
//            // Error Or 기존 member 초기화
//        } else {
//            member = memberService.register(oAuthLogin);
//        }
//        // 소셜 테이블에도 저장
//        SocialMember socialMember = SocialMember.of(member, oAuthLogin);
//        save(socialMember);
//        log.info("social 회원");
//        return new OauthMember(oAuthLogin, member);




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

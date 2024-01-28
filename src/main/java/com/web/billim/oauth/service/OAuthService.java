package com.web.billim.oauth.service;

import com.web.billim.exception.DuplicatedException;
import com.web.billim.exception.handler.ErrorCode;
import com.web.billim.member.domain.Member;
import com.web.billim.member.service.MemberDomainService;
import com.web.billim.member.service.MemberService;
import com.web.billim.oauth.domain.SocialMember;
import com.web.billim.oauth.dto.KakaoLogin;
import com.web.billim.oauth.dto.OAuthLogin;
import com.web.billim.oauth.dto.OAuthMember;
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
public class OAuthService extends DefaultOAuth2UserService {

    private static final String KAKAO_PROVIDER = "KAKAO";
    private final MemberService memberService;
    private final OAuthRepository oAuthRepository;
    private final MemberDomainService memberDomainService;

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
        validateProvider(userRequest);
        OAuthLogin oAuthLogin = KakaoLogin.ofKaKao(userRequest, oAuth2User.getAttributes());

        SocialMember socialMember;
        if (oAuthRepository.existsByAccountId(oAuthLogin.getProviderId())) {
            log.debug("기존 카카오톡 로그인 회원");
            socialMember = oAuthRepository.findByAccountId(oAuthLogin.getProviderId());
            oAuthRepository.save(socialMember.updateLoginInfo(oAuthLogin));
            // 여기서 RefreshToken, RefreshTokenExpiredAt 업데이트 해주기
        } else {
            log.debug("신규 카카오톡 로그인 회원");
            // member table 에 email 있으면 일반 로그인 회원으로 가입한 이력이 있음.
            if (memberDomainService.existByEmail(oAuthLogin.getEmail()) && memberDomainService.findByEmail(oAuthLogin.getEmail()).getUseYn().equals("Y")) {
                throw new DuplicatedException(ErrorCode.GENERAL_DUPLICATE_EMAIL);
            }
            Member member = memberService.register(oAuthLogin);  // member 테이블에 저장 -> 신규
            socialMember = oAuthRepository.save(SocialMember.of(member, oAuthLogin));
        }
        return new OAuthMember(socialMember);
    }

    private void validateProvider(OAuth2UserRequest userRequest) {
        String provider = userRequest.getClientRegistration().getClientName();

        if (!provider.equals(KAKAO_PROVIDER)) {
            throw new RuntimeException("지원하지 않는 OAuth2 Provider 입니다.");
        }
    }

//    public SocialMember save(SocialMember socialMember) {
//        return oAuthRepository.save(socialMember);
//    }
//
//    public Boolean existByAccountId(String accountId){
//        return oAuthRepository.existsByAccountId(accountId);
//    }

}

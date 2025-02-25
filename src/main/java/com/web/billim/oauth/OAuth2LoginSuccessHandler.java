package com.web.billim.oauth;

import com.web.billim.jwt.provider.JwtProvider;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.jwt.service.JwtService;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.oauth.dto.OAuthMember;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("카카오 로그인 성공");
        OAuthMember oauthMember = (OAuthMember) authentication.getPrincipal();
        long memberId = oauthMember.getMemberId();
        MemberGrade memberGrade = MemberGrade.valueOf(oauthMember.getGrade().name());

        String accessToken = jwtProvider.createAccessToken(String.valueOf(memberId), memberGrade);
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(memberId));

        RedisJwt redisJwt = new RedisJwt(memberId, refreshToken);
        jwtService.saveToken(redisJwt);

//        LoginResponse loginResponse = new LoginResponse(memberId, accessToken, refreshToken);
//        String body = new ObjectMapper().writeValueAsString(loginResponse);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(body);

        response.sendRedirect(UriComponentsBuilder.fromUriString("https://billim.store/login/callback")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("memberId", memberId)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString());
    }

    /** // TODO:
     * `state` 얘가 주범 ⇒ OAuth 로그인에서 얘가 왜 필요한지?
     * `AuthorizationRequestRepository` 라는 인터페이스의 구현체 `HttpSessionOAuth2AuthorizationRequestRepository` 라는게 있었다.
     * 	    나 카카오 로그인 페이지로 리다이렉션하게 경로줘
     * 		이 요청에 대한 기록을 `HttpSessionOAuth2AuthorizationRequestRepository` 이걸 통해서 저장해 뒀다가
     * 	    나 카카오한테 인가코드 받아왔어 이걸로 인증해줘
     * 		여기서 `HttpSessionOAuth2AuthorizationRequestRepository` 이걸 통해서 꺼내서 `state` 를 검증하게 된다.
     * `HttpSessionOAuth2AuthorizationRequestRepository` 어떤식으로 저장해줬을까?
     * 	    AuthorizationRequest 이 요청을 세션에 저장을 해준다.
     * 	    첫 번째 요청에서 받아온 세션키(JSESSIONID) 가 두 번째 요청에서 사라졌음… *(원인은 못찾음)*
     *      `HttpSessionOAuth2AuthorizationRequestRepository` 얘를 버리고 내가 직접만들자.
     * `AuthorizationRequestRepository` 얘가 필요하기는한데 세션을 사용하니까 좀 이상하다.. :(
     *      Scale-out 된 상황에서 세션을 이용하면 어차피 대응이 안된다. (세션저장소를 외부에 별도로 만들지 않는이상)
     *      일단은 세션이 아니라 그냥 `Map<>` 을 이용해서 메모리에 저장을 했다.
     *      근데 이거 실전에서 쓸려면 메모리에 올리는건 별로 안좋아보인다. (Scale-out 에 대응도 안되고)
     *      Redis 를 이용하는게 제일 좋아보임! 없으면 RDB..?
     *      뭘 저장했냐면? → `Map<state, AuthorizationRequest>` -> 이러면 잘 된다.
     *
     * — 현재 코드는
     * 백엔드로 리다이렉트를 받는다.
     * access_token, refresh_token 을 쿼리 파라미터로 전달하는게 좋아보이지 않음
     * 다른 전달 방법은 없을까..? (해결하고싶은 내용) // FIXME
     */



}

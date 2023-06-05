package com.web.billim.jwt;

import com.web.billim.jwt.dto.ReIssueTokenRequest;
import com.web.billim.jwt.dto.RedisJwt;
import com.web.billim.member.domain.Member;
import com.web.billim.member.dto.response.ReIssueTokenResponse;
import com.web.billim.member.repository.MemberRepository;
import com.web.billim.security.domain.UserDetailsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtUtils jwtUtils;
    private final JwtTokenRedisService jwtTokenRedisService;
    private final MemberRepository memberRepository;
    public ReIssueTokenResponse reIssueToken(String accessToken, String  refreshToken ) {
            // refreshToken 이 만료 됬다면??
        if(!jwtUtils.tokenValidation(refreshToken)){
            throw new RuntimeException("토큰 만료");
        }
            // accessTokne 에서 memberId 가져오기
        Authentication authentication = jwtUtils.getAuthentication(accessToken);
        Member member = memberRepository.findById(Long.parseLong(authentication.getPrincipal().toString()))
                .orElseThrow();

        RedisJwt redisJwt = jwtTokenRedisService.compareToken(member.getMemberId());

            // refresh token 일치 check
        if(!refreshToken.equals(redisJwt.getRefreshToken())){
            throw new RuntimeException();
        }
        // access token , refreshTOken 발급
        String accessTokenNew = jwtUtils.createAccessToken(String.valueOf(member.getMemberId()),member.getGrade());
        String refreshTokenNew = jwtUtils.createRefreshToken(String.valueOf(member.getMemberId()));
        return new ReIssueTokenResponse(accessTokenNew,refreshTokenNew);
    }
}

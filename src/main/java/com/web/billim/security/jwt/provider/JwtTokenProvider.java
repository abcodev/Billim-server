package com.web.billim.security.jwt.provider;

import com.web.billim.security.domain.UserDetailsDto;
import com.web.billim.security.jwt.domain.BillimAuthentication;
import com.web.billim.security.service.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {

    private final UserDetailServiceImpl userDetailsService;
    private final long ACCESS_TIME;
    private final long REFRESH_TIME;
    private final String secretKey;
    private Key key;


    public JwtTokenProvider(@Value("${jwt.secret}")String secretKey,
                            @Value("${jwt.access-time}") long ACCESS_TIME,
                            @Value("${jwt.refresh-time}") long  REFRESH_TIME,
                            UserDetailServiceImpl userDetailsService) {
        this.secretKey = secretKey;
        this.ACCESS_TIME = ACCESS_TIME;
        this.REFRESH_TIME = REFRESH_TIME;
        this.userDetailsService = userDetailsService;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    // Access Token 발급
    public String createAccessToken(String memberId){
        return Jwts.builder()
                .setHeaderParam("typ","ACCESS")
                .setSubject(memberId)
                .setAudience("BRONZE")
                .setIssuedAt(new Date(System.currentTimeMillis()+ACCESS_TIME))
                .setExpiration(new Date(System.currentTimeMillis()))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }

    // Refresh Token 발급
    public String createRefreshToken(String memberId){
        return Jwts.builder()
                .setHeaderParam("typ","REFRESH")
                .setSubject(memberId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+REFRESH_TIME))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }



    // 회원 정보 추출
    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        UserDetailsDto userDetails = (UserDetailsDto) userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
    }



    // token 검증
    public Boolean tokenValidation(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.error("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
            log.error("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e){
            log.error("지원하지 않는 JWT 토큰입니다.");
        }catch(IllegalArgumentException e){
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}

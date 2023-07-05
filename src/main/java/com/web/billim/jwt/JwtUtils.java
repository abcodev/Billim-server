package com.web.billim.jwt;

import com.web.billim.jwt.dto.JwtAuthenticationToken;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.security.domain.UserDetailsEntity;
import com.web.billim.security.UserDetailServiceImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils implements InitializingBean {

	private final UserDetailServiceImpl userDetailsService;
	private final long ACCESS_TIME;
	private final long REFRESH_TIME;
	private final String secretKey;
	private Key key;

	public JwtUtils(@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.access-time}") long ACCESS_TIME,
		@Value("${jwt.refresh-time}") long REFRESH_TIME,
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
	// GrantedAuthority
	public String createAccessToken(String memberId, MemberGrade memberGrade) {
		return Jwts.builder()
			.setHeaderParam("typ", "ACCESS")
			.setSubject(memberId)
			.setAudience(memberGrade.toString())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TIME))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	// Refresh Token 발급
	public String createRefreshToken() {
		return Jwts.builder()
			.setHeaderParam("typ", "REFRESH")
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TIME))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	// 회원 정보 추출
	public JwtAuthenticationToken getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
		UserDetailsEntity userDetails = userDetailsService.findByMemberId(Long.parseLong(claims.getSubject()));
		return new JwtAuthenticationToken(userDetails.getAuthorities(), userDetails.getMemberId());
	}

	// token 검증
	public Boolean tokenValidation(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.error("잘못된 JWT 서명입니다.");
			return false;
		} catch (ExpiredJwtException e) {
			log.error("만료된 JWT 토큰입니다.");
			return false;
		} catch (UnsupportedJwtException e) {
			log.error("지원하지 않는 JWT 토큰입니다.");
			return false;
		} catch (IllegalArgumentException e) {
			log.error("JWT 토큰이 잘못되었습니다.");
			return false;
		}



//		try {
//			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//			return true;
//		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//			// 1. 로그로 남기는 방법
//			// 2. 이 객체를 cause 로써 상위 예외 객체에 포함시킨다.
//			log.error("잘못된 JWT 서명입니다.", e);
//			return false;
//		} catch (UnsupportedJwtException e) {
//			log.error("지원하지 않는 JWT 토큰입니다.", e);
//			return false;
//		} catch (IllegalArgumentException e) {
//			log.error("JWT 토큰이 잘못되었습니다.", e);
//			return false;
//		}



	}

}

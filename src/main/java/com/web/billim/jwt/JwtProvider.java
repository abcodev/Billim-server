package com.web.billim.jwt;

import com.web.billim.common.exception.JwtException;
import com.web.billim.common.exception.handler.ErrorCode;
import com.web.billim.jwt.dto.JwtAuthenticationToken;
import com.web.billim.member.type.MemberGrade;
import com.web.billim.security.domain.UserDetailsEntity;
import com.web.billim.security.UserDetailServiceImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider implements InitializingBean {

	private final UserDetailServiceImpl userDetailsService;
	private final long ACCESS_TIME;
	private final long REFRESH_TIME;
	private final String secretKey;
	private Key key;

	public JwtProvider(@Value("${jwt.secret}") String secretKey,
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
//				.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TIME))
				.setExpiration(new Date(System.currentTimeMillis() + 120000))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}

	// Refresh Token 발급
	public String createRefreshToken(String memberId) {
		return Jwts.builder()
				.setHeaderParam("typ", "REFRESH")
				.setSubject(memberId)
				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TIME))
				.setExpiration(new Date(System.currentTimeMillis() + 300000))
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


	public boolean tokenValidation(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().
					parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			log.error("wrong signature JWT", ex);
			throw new JwtException(ErrorCode.INVALID_TOKEN);
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token", ex);
			throw new JwtException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token", ex);
			throw new JwtException(ErrorCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token", ex);
			throw new JwtException(ErrorCode.UNSUPPORTED_TOKEN);
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty", ex);
			throw new JwtException(ErrorCode.UNKNOWN_ERROR);
		}
	}
}





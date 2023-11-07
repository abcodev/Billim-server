package com.web.billim.oauth.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;
import com.web.billim.oauth.dto.OAuthLogin;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "social_member")
@Getter
@Builder
public class SocialMember extends JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_id")
    private Long socialId;

    private String providerName;
    private String accountId;

    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    @OneToOne
    private Member member;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiredAt;

    public static SocialMember of(Member member, OAuthLogin oAuthLogin){
        return SocialMember.builder()
                .providerName(oAuthLogin.getProvider())
                .accountId(oAuthLogin.getProviderId())
                .member(member)
                .refreshToken(oAuthLogin.getRefreshToken())
                .refreshTokenExpiredAt(oAuthLogin.getRefreshTokenExpiredAt())
                .build();
    }

    public SocialMember updateLoginInfo(OAuthLogin oAuthLogin) {
        this.refreshToken = oAuthLogin.getRefreshToken();
        this.refreshTokenExpiredAt = oAuthLogin.getRefreshTokenExpiredAt();
//        this.accessAt = LocalDateTime.now();
        return this;
    }
}



package com.web.billim.security.oauth;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;
import lombok.*;

import javax.persistence.*;
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

    public static SocialMember of(Member member, OAuthLogin oAuthLogin){
        return SocialMember.builder()
                .providerName(oAuthLogin.getProvider())
                .accountId(oAuthLogin.getProviderId())
                .member(member)
                .build();
    }
}



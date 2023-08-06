package com.web.billim.security.oauth;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "social_member")
@Getter
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
}

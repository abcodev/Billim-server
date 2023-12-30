package com.web.billim.oauth.repository;

import com.web.billim.member.domain.Member;
import com.web.billim.oauth.domain.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthRepository extends JpaRepository<SocialMember,Long> {
    Boolean existsByAccountId(String accountId);

    SocialMember findByAccountId(String providerId);

    Optional<SocialMember> findByMember(Member member);
}

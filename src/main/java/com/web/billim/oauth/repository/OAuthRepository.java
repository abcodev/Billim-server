package com.web.billim.oauth.repository;

import com.web.billim.oauth.domain.SocialMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRepository extends JpaRepository<SocialMember,Long> {
    Boolean existsByAccountId(String accountId);

    SocialMember findByAccountId(String providerId);
}

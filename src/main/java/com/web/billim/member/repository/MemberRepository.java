package com.web.billim.member.repository;

import com.web.billim.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByNickname(String nickname);
    boolean existsByUserId(String userId);
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByNameAndEmail(String name, String email);

}

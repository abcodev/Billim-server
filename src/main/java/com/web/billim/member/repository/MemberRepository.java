package com.web.billim.member.repository;

import com.web.billim.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findByEmailAndName(String email, String name);
    Member findByEmail(String email);

    void deleteByProfileImageUrl(String deleteProfileImage);

    @Query("select m.memberId as memberId from Member m")
    List<Long> findAllMemberId();

}

package com.web.billim.member.repository;

import com.web.billim.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

//    Optional<Member> findByEmailAndName(String email, String name);
    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.name = :name AND m.useYn = 'Y'")
    Optional<Member> findByEmailAndName(@Param("email") String email, @Param("name") String name);

    Member findByEmail(String email);

//    void deleteByProfileImageUrl(String deleteProfileImage);

    @Query("select m.memberId as memberId from Member m")
    List<Long> findAllMemberId();

    @Query("select m.useYn as useYn from Member m where m.email = :email")
    String resignCheck(@Param("email") String email);

}

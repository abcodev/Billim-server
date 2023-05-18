package com.web.billim.point.repository;

import com.web.billim.member.domain.Member;
import com.web.billim.point.domain.SavedPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedPointRepository extends JpaRepository<SavedPoint, Long> {

	@Query("SELECT sp FROM SavedPoint sp WHERE sp.member.id = :memberId "
			+ "AND sp.expiredAt >= current_timestamp ORDER BY sp.expiredAt DESC")
	List<SavedPoint> findAllNotExpired(@Param("memberId") long memberId);

	List<SavedPoint> findAllByMember(Member member);
}

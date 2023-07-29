package com.web.billim.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.billim.chat.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

	@Query("SELECT cr FROM ChatRoom cr WHERE cr.product.productId = :productId AND cr.buyer.memberId = :buyerId")
	Optional<ChatRoom> findByProductIdAndBuyerId(@Param("productId") long productId, @Param("buyerId") long buyerId);

	@Query("SELECT cr FROM ChatRoom cr WHERE cr.product.productId = :productId")
	List<ChatRoom> findAllByProductId(@Param("productId") long productId);

	@Query("SELECT cr FROM ChatRoom cr WHERE cr.buyer.memberId = :buyerId AND cr.buyerJoined = true")
	List<ChatRoom> findAllJoinedByBuyerId(@Param("buyerId") long buyerId);

	@Query("SELECT cr FROM ChatRoom cr WHERE cr.seller.memberId = :sellerId AND cr.sellerJoined = true")
	List<ChatRoom> findAllJoinedBySellerId(@Param("sellerId") long sellerId);

}

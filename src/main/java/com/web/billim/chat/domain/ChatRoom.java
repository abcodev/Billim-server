package com.web.billim.chat.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;
import com.web.billim.product.domain.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "chat_room")
@Builder
@Getter
public class ChatRoom extends JpaEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "seller_id", referencedColumnName = "member_id")
	private Member seller;

	@Column(name = "seller_joined_yn")
	private boolean sellerJoined;

	@ManyToOne
	@JoinColumn(name = "buyer_id", referencedColumnName = "member_id")
	private Member buyer;

	@Column(name = "buyer_joined_yn")
	private boolean buyerJoined;

	public static ChatRoom of(Member member, Product product) {
		return ChatRoom.builder()
			.product(product)
			.seller(product.getMember())
			.sellerJoined(true)
			.buyer(member)
			.buyerJoined(true)
			.build();
	}

	public Member exit(long memberId) {
		if (seller.getMemberId() == memberId) {
			sellerJoined = false;
			return seller;
		} else if (buyer.getMemberId() == memberId) {
			buyerJoined = false;
			return buyer;
		}
		throw new RuntimeException("채팅방에 참여중이지 않은 사용자입니다.");
	}

	public ChatRoom reJoin() {
		this.buyerJoined = true;
		this.sellerJoined = true;
		return this;
	}

	public boolean checkEmpty() {
		return this.sellerJoined && this.buyerJoined;
	}

}

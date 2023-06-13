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

	@ManyToOne
	@JoinColumn(name = "buyer_id", referencedColumnName = "member_id")
	private Member buyer;

	public static ChatRoom of(Member member, Product product) {
		return ChatRoom.builder()
			.product(product)
			.seller(product.getMember())
			.buyer(member)
			.build();
	}

	public boolean isSellerExit() {
		return seller == null;
	}

	public Member exit(long memberId) {
		Member exitMember = null;
		if (seller.getMemberId() == memberId) {
			exitMember = seller;
			seller = null;
		} else if (buyer.getMemberId() == memberId) {
			exitMember = buyer;
			buyer = null;
		}
		return exitMember;
	}

}

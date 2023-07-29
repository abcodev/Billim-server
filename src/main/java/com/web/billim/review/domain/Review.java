package com.web.billim.review.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.order.domain.ProductOrder;
import lombok.*;
import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "review")
@Builder
@Getter
public class Review extends JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @JoinColumn(name = "product_order_id",referencedColumnName = "product_order_id")
    @OneToOne
    private ProductOrder productOrder;

    private String content;

    private long starRating;


// 1. 리뷰가 order 를 가지고 있다면 status 가 필요할까?
//  -> order 도 review 를 가지고 있어야 한다면 order를 저장할때 review가 필요하기 때문에 이건 안됨.
//  -> order 를 불러올때 order 번호로된 review 가 있는지 찾고 없다면 리뷰를 달지 않았다는 말.
//  -> 즉, review는 order 를 가지고 order는 review 를 가지고 있지 않아도 된다.
//  -> review 에 상태는 존재하지 않아도 된다.



//    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
//    @ManyToOne
//    private Product product;
//
//    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
//    @ManyToOne
//    private Member member;
}

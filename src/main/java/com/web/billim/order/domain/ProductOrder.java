package com.web.billim.order.domain;

import com.web.billim.common.domain.JpaEntity;
import com.web.billim.member.domain.Member;
import com.web.billim.order.dto.OrderCommand;
import com.web.billim.order.type.ProductOrderStatus;
import com.web.billim.order.vo.Period;
import com.web.billim.product.domain.Product;
import com.web.billim.product.type.TradeMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_order")
@Builder
@Getter
public class ProductOrder extends JpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id")
    private Long orderId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    private Product product;

    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    @ManyToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    private TradeMethod tradeMethod;

    @Enumerated(EnumType.STRING)
    private ProductOrderStatus status;

    private LocalDate startAt;

    private LocalDate endAt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="name", column=@Column(name="buyer_name")),
            @AttributeOverride(name="address", column=@Column(name="buyer_address")),
            @AttributeOverride(name="phone", column=@Column(name="buyer_phone"))
    })
    private ProductBuyer buyer;

    public Period getPeriod() {
        return new Period(startAt, endAt);
    }

    public long getPrice() {
        long productPrice = this.product.getPrice();
        long rentDays = java.time.Period.between(this.startAt, this.endAt).getDays() + 1;
        return productPrice * rentDays;
    }

    public static ProductOrder generateNewOrder(Member member, Product product, OrderCommand command) {
        ProductOrderBuilder order = ProductOrder.builder()
                .product(product)
                .member(member)
                .startAt(command.getStartAt())
                .endAt(command.getEndAt())
                .tradeMethod(command.getTradeMethod())
                .status(ProductOrderStatus.IN_PROGRESS);

        if (command.getTradeMethod().equals(TradeMethod.DELIVERY)) {
            ProductBuyer buyer = new ProductBuyer(command.getName(), command.getAddress(), command.getPhone());
            order.buyer(buyer);
        }
        return order.build();
    }

    public void cancel() {
        this.status = ProductOrderStatus.CANCELED;
    }

    public void complete() {
        this.status = ProductOrderStatus.DONE;
    }

    public boolean isCanceled() {
        return this.status == ProductOrderStatus.CANCELED;
    }
}


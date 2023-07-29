package com.web.billim.order.repository;

import com.web.billim.member.domain.Member;
import com.web.billim.order.domain.ProductOrder;
import com.web.billim.order.type.ProductOrderStatus;
import com.web.billim.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<ProductOrder, Long> {
    List<ProductOrder> findAllByProductAndEndAtAfter(Product product, LocalDate now);
    Optional<ProductOrder> findByMemberAndStatus(Member member, ProductOrderStatus status);
    Optional<ProductOrder> findByProductAndStatus(Product product, ProductOrderStatus status);

    List<ProductOrder> findAllByMember_memberId_OrderByOrderIdDesc(long memberId);
    Optional<Long> countByMember_memberId(long memberId);

	List<ProductOrder> findAllByProduct(Product product);

	List<ProductOrder> findAllByEndAt(LocalDate datetime);

    List<ProductOrder> findAllByMember_memberId(long memberId);
}


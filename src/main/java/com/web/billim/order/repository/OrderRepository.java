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

//    @Query("SELECT COUNT(po) FROM ProductOrder po WHERE po.member.memberId = :memberId AND po.status = 'DONE'")
//    @Query("SELECT COUNT(po) FROM ProductOrder po WHERE po.member.memberId = :memberId AND po.status = 'DONE' AND po.endAt <= CURRENT_DATE")
    @Query("SELECT COUNT(po) FROM ProductOrder po WHERE po NOT IN (SELECT r.productOrder FROM Review r) AND po.status = 'DONE' AND po.endAt <= CURRENT_DATE")
    Optional<Long> countByMemberAndStatus(long memberId);

	List<ProductOrder> findAllByProduct(Product product);

	List<ProductOrder> findAllByEndAt(LocalDate datetime);

    List<ProductOrder> findAllByMember_memberId(long memberId);


//    @Query("SELECT po FROM ProductOrder po WHERE po NOT IN (SELECT r.productOrder FROM Review r)")
//    List<ProductOrder> findProductOrdersWithoutReview();

    @Query("SELECT po FROM ProductOrder po WHERE po NOT IN (SELECT r.productOrder FROM Review r) AND po.status = 'DONE' AND po.endAt <= CURRENT_DATE")
    List<ProductOrder> findProductOrdersWritableReview(long memberId);

}


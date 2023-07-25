package com.web.billim.payment.repository;

import com.web.billim.order.domain.ProductOrder;
import com.web.billim.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMerchantUid(String merchantUid);

	Optional<Payment> findByProductOrder(ProductOrder order);
}

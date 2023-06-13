package com.web.billim.product.repository;

import com.web.billim.product.domain.ProductInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInterestRepository extends JpaRepository<ProductInterest,Long> {


}

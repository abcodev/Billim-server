package com.web.billim.product.repository;

import com.web.billim.product.domain.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageProductRepository extends JpaRepository<ImageProduct, Integer> {
}

package com.web.billim.product.repository;

import com.web.billim.product.domain.ImageProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {

	@Modifying
	@Transactional
	void deleteByUrl(String url);

}

package com.web.billim.product.repository;

import com.web.billim.order.dto.response.MySalesListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.web.billim.product.domain.Product;

public interface ProductQueryDslRepository {
	Page<Product> findAllByKeyword(String category, String keyword, Pageable pageable);
}

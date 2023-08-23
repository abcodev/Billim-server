package com.web.billim.product.repository;

import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByCategoryName(String category);

//    @Query("SELECT p FROM Product p left join fetch p.member where p.productId = :productId")
//    Optional<Product> createQuery(long productId);

}

package com.web.billim.product.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.billim.product.domain.Product;
import com.web.billim.product.domain.QProduct;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductQueryDslRepositoryImpl implements ProductQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QProduct product = QProduct.product;

	// @Query("SELECT p FROM Product p "
	//     + "WHERE p.productName like %:keyword% OR p.detail like %:keyword% ORDER BY p.createdAt DESC")
	@Override
	public Page<Product> findAllByKeyword(String category, String keyword, Pageable pageable) {
		var count = Optional.ofNullable(jpaQueryFactory.select(product.count())
			.from(product)
			.where(product.isDeleted.eq(false))
			.where(product.productName.contains(keyword).or(product.detail.contains(keyword)))
			.fetchOne()).orElse(0L);

		var productList = jpaQueryFactory.selectFrom(product)
			.innerJoin(product.member).fetchJoin()
			.innerJoin(product.productCategory).fetchJoin()
			.where(product.isDeleted.eq(false))
			.where(this.checkCategoryName(category))
			.where(product.productName.contains(keyword).or(product.detail.contains(keyword)))
			.orderBy(product.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return PageableExecutionUtils.getPage(productList, pageable, () -> count);
	}

	private BooleanExpression checkCategoryName(String category) {
		return !category.isEmpty() ? product.productCategory.categoryName.eq(category) : null;
	}

}

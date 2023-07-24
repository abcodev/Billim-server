package com.web.billim.order.dto.response;

import com.web.billim.product.domain.Product;
import com.web.billim.product.type.TradeMethod;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MySalesListResponse {

	private long productId;
	private String productName;
	private long price;
	private String imageUrl;
	private List<TradeMethod> tradeMethods;

	public static MySalesListResponse of(Product product) {
		return MySalesListResponse.builder()
			.productId(product.getProductId())
			.productName(product.getProductName())
			.price(product.getPrice())
			.imageUrl(product.mainImage())
			.tradeMethods(product.getTradeMethods())
			.build();
	}

}

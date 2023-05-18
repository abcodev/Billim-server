package com.web.billim.product.dto.response;

import com.web.billim.product.domain.ImageProduct;
import com.web.billim.product.domain.Product;
import com.web.billim.product.type.TradeMethod;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MyProductSalesResponse {

	private long productId;
	private String productName;
	private String detail;
	private long price;
	private List<String> imageUrls;
	private List<TradeMethod> tradeMethods;

	public static MyProductSalesResponse of(Product product) {
		return MyProductSalesResponse.builder()
			.productId(product.getProductId())
			.productName(product.getProductName())
			.detail(product.getDetail())
			.price(product.getPrice())
			.imageUrls(
				product.getImages().stream()
					.map(ImageProduct::getUrl)
					.collect(Collectors.toList())
			)
			.tradeMethods(product.getTradeMethods())
			.build();
	}

}

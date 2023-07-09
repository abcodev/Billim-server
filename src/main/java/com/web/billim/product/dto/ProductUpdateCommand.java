package com.web.billim.product.dto;

import com.web.billim.product.dto.request.ProductUpdateRequest;
import com.web.billim.product.type.TradeMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateCommand {

    private Long productId;
    private String category;
    private Long memberId;
    private String rentalProduct;
    private String description;
    private Long rentalFee;
    private List<MultipartFile> images;
    private List<TradeMethod> tradeMethods;
    private String place;

    public ProductUpdateCommand(ProductUpdateRequest req) {
        this.productId = req.getProductId();
        this.category = req.getCategory();
        this.rentalProduct = req.getRentalProduct();
        this.description = req.getDescription();
        this.rentalFee = req.getRentalFee();
        this.images = Stream.of(req.getImage0(), req.getImage1(), req.getImage2(), req.getImage3(), req.getImage4())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        this.tradeMethods = req.getTradeMethods();
        this.place = req.getPlace();
    }

}

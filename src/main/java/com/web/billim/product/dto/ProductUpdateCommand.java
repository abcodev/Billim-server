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

    private long productId;
    private String category;
    private long memberId;
    private String productName;
    private String productDetail;
    private long price;

    private List<String> deleteImageUrls;
    private List<MultipartFile> appendImages;
    private List<TradeMethod> tradeMethods;
    private String tradeArea;

    public ProductUpdateCommand(ProductUpdateRequest req) {
        this.productId = req.getProductId();
        this.category = req.getCategory();
        this.productName = req.getRentalProduct();
        this.productDetail = req.getDescription();
        this.price = req.getRentalFee();
        this.deleteImageUrls = req.getDeleteImages();
        this.appendImages = Stream.of(req.getImage0(), req.getImage1(), req.getImage2(), req.getImage3(), req.getImage4())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        this.tradeMethods = req.getTradeMethods();
        this.tradeArea = req.getPlace();
    }

}

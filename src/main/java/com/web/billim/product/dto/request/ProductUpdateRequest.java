package com.web.billim.product.dto.request;

import com.web.billim.product.type.TradeMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductUpdateRequest {

    private long productId;
    private long memberId;

    @NotEmpty(message = "카테고리는 필수 입력입니다.")
    private String category;

    @NotEmpty(message = "상품명은 필수정보입니다.")
    private String rentalProduct;

    @NotEmpty(message = "상품설명은 필수 입력입니다.")
    private String description;

    @Positive
    @NotNull(message = "가격정보는 필수입니다.")
    @Min(value = 100, message = "상품 금액은 100원 이상 입력되어야 합니다.")
    private long rentalFee;

//    @NotEmpty(message = "이미지는 한 장 이상 첨부해 주세요.")
    private List<String> deleteImages;

    private MultipartFile image0;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    private MultipartFile image4;

    @NotEmpty(message = "거래 방법은 필수 입력 입니다.")
    private List<TradeMethod> tradeMethods;

    private String place;

    public void setRegisterMember(long memberId) {
        this.memberId = memberId;
    }

}

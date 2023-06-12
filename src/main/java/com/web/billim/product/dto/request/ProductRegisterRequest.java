package com.web.billim.product.dto.request;

import com.web.billim.product.type.TradeMethod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@ApiModel(value = "상품 등록 요청")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRegisterRequest {

    @Positive
    private long categoryId;

    private long memberId;

    @ApiModelProperty(value = "상품명")
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "상품 설명")
    @NotEmpty
    private String detail;

    @Positive
    @NotEmpty
    @Min(value = 100, message = "상품 금액은 100원 이상 입력되어야 합니다.")
    private long price;

    @NotEmpty
    private List<MultipartFile> images;

    @NotEmpty
    private List<TradeMethod> tradeMethods;

    private String tradeArea;

    public void setRegisterMember(long memberId) {
        this.memberId = memberId;
    }
}

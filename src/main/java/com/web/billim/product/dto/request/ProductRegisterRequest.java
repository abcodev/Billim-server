package com.web.billim.product.dto.request;

import com.web.billim.product.type.TradeMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRegisterRequest {

    @Positive // 양수만 가능
    private int categoryId;

    private int memberId;

    @NotEmpty
//	@NotBlank(message = "대여 상품명은 필수 항목입니다.")
    private String name;

    @NotEmpty
//	@NotBlank(message = "상품 설명은 필수 항목입니다.")
    private String detail;

//    @NotBlank(message = "금액은 필수항목입니다. 100원 이상 입력해 주세요.")
    @Positive
    @Min(value = 100, message = "100원 이상 입력해 주세요.")
    private int price;

//    @NotBlank(message = "필수입력")
    private List<MultipartFile> images;

//    @NotBlank(message = "필수입력")
//    @Min(value = 5, message = "사진을 5장 첨부 해 주세요.")
    private List<TradeMethod> tradeMethods;

    public void setRegisterMember(int memberId) {
        this.memberId = memberId;
    }
}

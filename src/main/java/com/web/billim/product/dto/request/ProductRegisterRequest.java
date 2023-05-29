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

    @Positive
    private long categoryId;

    private long memberId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String detail;

    @Positive
    @NotEmpty
    @Min(value = 100, message = "100원 이상 입력해 주세요.")
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

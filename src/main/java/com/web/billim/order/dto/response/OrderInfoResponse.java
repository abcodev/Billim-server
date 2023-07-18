package com.web.billim.order.dto.response;

import com.web.billim.product.type.TradeMethod;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import io.swagger.annotations.ApiOperation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
//@ApiModel(value = "주문 정보")
public class OrderInfoResponse {

    private int productId;
    private String productName;
    private String detail;
//    @ApiModelProperty(value = "총 금액")
    private long price;
    private List<TradeMethod> tradeMethods;

}

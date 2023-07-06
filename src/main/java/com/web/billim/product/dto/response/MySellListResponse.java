package com.web.billim.product.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MySellListResponse {

    private long memberId;
    private long productId;
    private String productName;


}

package com.web.billim.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestRequest {
    private long productId;
    private Boolean interest;
}

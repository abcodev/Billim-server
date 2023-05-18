package com.web.billim.client.iamport.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IamPortPaymentResponse {

    private long code;
    private String message;
    private IamPortPaymentData response;

}

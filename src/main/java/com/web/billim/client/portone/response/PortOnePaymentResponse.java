package com.web.billim.client.portone.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortOnePaymentResponse {

    private String status;
    private String transactionId;
    private String orderName;

}

package com.web.billim.client.iamport.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IamPortAccessTokenResponse {

    private long code;
    private String message;
    private IamPortAccessTokenData response;

}

package com.web.billim.client.iamport.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IamPortPaymentCancelResponse {

	private String impUid;
	private String merchantUid;

}

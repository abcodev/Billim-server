package com.web.billim.client.iamport;

import com.web.billim.client.iamport.response.IamPortPaymentData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IamPortClientService {

	private final IamPortClient iamPortClient;

	public String generateMerchantUid() {
		return UUID.randomUUID().toString();
	}

	public IamPortPaymentData retrievePayment(String impUid) {
		return iamPortClient.retrievePaymentHistory(impUid);
	}

}

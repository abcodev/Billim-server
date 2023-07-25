package com.web.billim.client.iamport;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.web.billim.client.iamport.response.IamPortPaymentData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IamPortClientService {

	private final IamPortClient iamPortClient;

	public String generateMerchantUid() {
		return UUID.randomUUID().toString();
	}

	public boolean validate(String impUid, long totalAmount) {
		IamPortPaymentData paymentData = iamPortClient.retrievePaymentHistory(impUid);
		return totalAmount == paymentData.getAmount() && paymentData.getStatus().equals("paid");
	}

	public void cancel(String impUid) {
		iamPortClient.cancel(impUid);
	}

}

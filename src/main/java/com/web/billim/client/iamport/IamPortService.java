package com.web.billim.client.iamport;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class IamPortService {

	public String generateMerchantUid() {
		// UUID v4
		return UUID.randomUUID().toString();
	}

}

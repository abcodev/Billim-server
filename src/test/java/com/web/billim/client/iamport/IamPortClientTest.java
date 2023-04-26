package com.web.billim.client.iamport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IamPortClientTest {

	@Autowired
	private IamPortClient iamPortClient;

	@Test
	public void getAccessToken_테스트() {
		System.out.println(iamPortClient.getAccessToken());
	}

}
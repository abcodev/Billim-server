package com.web.billim;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
//		(exclude = {
//		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
//		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class
//})
public class BillimApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("KST"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BillimApplication.class, args);
	}

}

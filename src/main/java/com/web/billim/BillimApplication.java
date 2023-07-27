package com.web.billim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class
})
public class BillimApplication {

	public static void main(String[] args) {

		SpringApplication.run(BillimApplication.class, args);
	}

}

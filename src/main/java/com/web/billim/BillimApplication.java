package com.web.billim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class BillimApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillimApplication.class, args);
	}

}

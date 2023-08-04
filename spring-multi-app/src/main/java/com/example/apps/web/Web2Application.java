package com.example.apps.web;

import com.example.service.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Web2Application {

	@Bean
	public HelloService helloService() throws InterruptedException {
		System.out.println("create hello service");
		Thread.sleep(10*1000);
		return new HelloService();
	}

	public static void main(String[] args) {
		SpringApplication.run(Web2Application.class, args);
	}

}

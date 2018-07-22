package com.example.springbootsecond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private HelloMessageService helloService;


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args){
		if (args.length > 0)
			System.out.println(helloService.getMessage(args[0]));
		else
			System.out.println(helloService.getMessage());

		System.exit(0);
	}
}

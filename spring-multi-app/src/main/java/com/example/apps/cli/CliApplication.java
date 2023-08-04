package com.example.apps.cli;

import com.example.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CliApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplicationBuilder builder = new SpringApplicationBuilder()
				.sources(CliApplication.class)
				.bannerMode(Banner.Mode.OFF);

		SpringApplication app = builder.build();
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args).close();
	}

	@Autowired
	HelloService helloService;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(helloService.hi());
	}
}

package com.server.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FitnessEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessEurekaApplication.class, args);
	}

}

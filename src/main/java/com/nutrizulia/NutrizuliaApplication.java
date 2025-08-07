package com.nutrizulia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NutrizuliaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutrizuliaApplication.class, args);
	}

}

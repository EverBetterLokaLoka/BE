package com.example.lokaloka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.lokaloka.repository")
public class LokalokaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LokalokaApplication.class, args);
	}

}

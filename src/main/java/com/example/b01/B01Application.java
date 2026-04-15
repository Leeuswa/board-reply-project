package com.example.b01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Auditing 기능 활성화 (시간 자동 저장 기능 ON)
public class B01Application {

	public static void main(String[] args) {
		SpringApplication.run(B01Application.class, args);
	}

}

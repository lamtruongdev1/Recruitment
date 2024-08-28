package com.poly.Recruitment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.poly.Recruitment")
public class DatnRecruitmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatnRecruitmentApplication.class, args);
	}

}

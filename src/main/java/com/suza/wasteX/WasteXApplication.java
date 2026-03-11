package com.suza.wasteX;


import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(scanBasePackages = "com.suza.wasteX")
@EnableJpaAuditing
public class WasteXApplication {

	public static void main(String[] args) {
		SpringApplication.run(WasteXApplication.class, args);



	}
}

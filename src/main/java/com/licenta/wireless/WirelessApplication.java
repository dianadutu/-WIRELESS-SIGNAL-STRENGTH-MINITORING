package com.licenta.wireless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WirelessApplication {

	public static void main(String[] args) {
		SpringApplication.run(WirelessApplication.class, args);
	}

}

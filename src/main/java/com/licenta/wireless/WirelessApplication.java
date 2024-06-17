package com.licenta.wireless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude= SecurityAutoConfiguration.class)
@EnableAsync
@EnableScheduling

public class WirelessApplication {

	public static void main(String[] args) {
		SpringApplication.run(WirelessApplication.class, args);
	}

}

package com.client.ws.rasmooplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class WsRasmooPlusApplication {
	public static void main(String[] args) {
		SpringApplication.run(WsRasmooPlusApplication.class, args);
	}

}

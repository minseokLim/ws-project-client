package com.wsproject.clientsvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ClientsvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientsvrApplication.class, args);
	}
}

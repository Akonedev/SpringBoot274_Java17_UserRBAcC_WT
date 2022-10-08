package org.akon.userapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserappApplication  implements CommandLineRunner {

	@Value("${jwt.secret}")
	private String jwt;
	public static void main(String[] args) {
		SpringApplication.run(UserappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("property jwt value is: " + jwt);
	}
}

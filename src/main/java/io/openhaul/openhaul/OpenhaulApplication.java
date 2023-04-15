package io.openhaul.openhaul;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OpenhaulApplication {

	@Value("${jwt.secret}")
	private String secret;

	public static void main(String[] args) {
		SpringApplication.run(OpenhaulApplication.class, args);
	}

	@Bean
	public PasswordEncoder getPasswordEncoderBean() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public Algorithm getAlgorithmBean() {
		return Algorithm.HMAC256(secret.getBytes());
	}

}

package pearl.ch.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PearlChServicesSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(PearlChServicesSpringApplication.class, args);
	}
	
}
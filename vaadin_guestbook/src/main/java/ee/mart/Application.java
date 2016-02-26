package ee.mart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import ee.mart.model.GuestEntry;
import ee.mart.service.GuestbookService;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
@ComponentScan
public class Application {
	
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Bean
	public CommandLineRunner dummy(GuestbookService service) {
		return (args) -> {
			service.save(new GuestEntry("Jane", "Doe", "It's test text"));
			service.save(new GuestEntry("Jaan", "Maasikas", "Another text"));
			service.save(new GuestEntry("Peeter", "Meeter", "Some kind of text"));
		};
	}
}

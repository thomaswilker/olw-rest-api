package olw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableEntityLinks;

@SpringBootApplication
@EnableEntityLinks
public class OlwRestApiApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(OlwRestApiApplication.class, args);
	}
    
    
}

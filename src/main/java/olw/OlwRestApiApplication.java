package olw;

import java.util.Arrays;
import java.util.List;

import olw.model.Area;
import olw.model.Assistant;
import olw.model.Collection;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Material;
import olw.model.Tag;
import olw.repository.AreaRepository;
import olw.repository.CollectionRepository;
import olw.repository.IndexedMaterialRepository;
import olw.repository.LecturerRepository;
import olw.repository.MaterialRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.hateoas.config.EnableEntityLinks;

@SpringBootApplication
@EntityScan(basePackageClasses = { OlwRestApiApplication.class})
@EnableEntityLinks
public class OlwRestApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		
		ApplicationContext context = SpringApplication.run(OlwRestApiApplication.class, args);
	}
    
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OlwRestApiApplication.class);
	}
}

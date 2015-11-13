package olw.importer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableEntityLinks;

import olw.OlwRestApiApplication;
import olw.model.Area;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Semester;

@SpringBootApplication
@EnableEntityLinks
@EnableJpaRepositories
public class ImportApplication {

	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = SpringApplication.run(OlwRestApiApplication.class, args);
		ImporterService importer = context.getBean(ImporterService.class);
		//importer.importMaterials();
		//importer.importEntity(License.class);
		//importer.importEntity(Lecturer.class);
		//importer.importEntity(Language.class);
		importer.importEntity(Semester.class);
		
		
	}
	
}

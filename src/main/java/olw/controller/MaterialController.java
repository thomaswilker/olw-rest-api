package olw.controller;

import java.util.List;

import olw.repository.IndexedMaterialRepository;
import olw.repository.MaterialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/materials")
@RepositoryRestController
public class MaterialController {

	@Autowired
	MaterialRepository repository;
	
	@Autowired
	List<JpaRepository<?, Long>> repositorys;
	
	@Autowired
	IndexedMaterialRepository indexRepository;
	
	
	@RequestMapping("/init")
	public void initMaterials() {
		
		indexRepository.deleteAll();
		repositorys.forEach(r -> r.deleteAll());
		
//		Lecturer lecturer = new Lecturer(1l, "Thomas", "Wilker");
//		
//		Assistant assistant = new Assistant("Erik", "Schnellbacher");
//		lecturer.getAssistants().add(assistant);
//		
//		Material m1 = new Material("Vorlesung 1", "Beschreibung");
//		m1.getLanguages().add(new Language("Deutsch"));
//		m1.getLecturer().add(lecturer);
//		m1.setLicense(new License("CC"));
//		
//		materialRepo.save(m1);
//		
//		CollectionRepository collectionRepo = context.getBean(CollectionRepository.class);
//		Collection c = new Collection();
//		c.getAreas().addAll(Arrays.asList(new Area("Informatik"), new Area("Maschinenbau")));
//		c.getTags().addAll(Arrays.asList(new Tag("Informatik")));
//		c.setName("Grundlagen der Informatik II");
//		collectionRepo.save(c);
		
		
	}
	
}

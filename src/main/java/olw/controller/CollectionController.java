package olw.controller;

import java.util.Arrays;

import olw.model.Area;
import olw.model.Assistant;
import olw.model.Collection;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Material;
import olw.repository.CollectionRepository;
import olw.repository.MaterialRepository;
import olw.repository.index.IndexedCollectionRepository;
import olw.repository.index.IndexedMaterialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/collections")
@RepositoryRestController
public class CollectionController {

	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	CollectionRepository collectionRepository;
	
	@Autowired
	IndexedCollectionRepository indexedCollectionRepository;
	
	@Autowired
	IndexedMaterialRepository indexedMaterialRepository;
	
	@RequestMapping("/init")
	public ResponseEntity<Collection> initMaterials() {
		
		indexedCollectionRepository.deleteAll();
		indexedMaterialRepository.deleteAll();
		
		Lecturer lecturer = new Lecturer(1l, "Thomas", "Wilker");
		Assistant assistant = new Assistant(1l, "Erik", "Schnellbacher");
		lecturer.getAssistants().add(assistant);
		
		Material m1 = new Material("Vorlesung 1", "Beschreibung");
		m1.getLanguages().add(new Language(1l, "Deutsch"));
		m1.getLecturers().add(lecturer);
		m1.setLicense(new License("CC"));
		m1.getTags().addAll(Arrays.asList("Kürzeste Wege","Graphentheorie","Dijkstra"));
		materialRepository.save(m1);
		
		Collection c = new Collection();
		c.getAreas().addAll(Arrays.asList(new Area(1l,"Informatik"), new Area(2l,"Maschinenbau")));
		c.getTags().addAll(Arrays.asList("Informatik","Grundlagen","Graphentheorie"));
		c.getMaterials().add(m1);
		c.setName("Grundlagen der Informatik II");
		
		return new ResponseEntity<Collection>(collectionRepository.save(c), HttpStatus.ACCEPTED);
		
		
	}

	
}

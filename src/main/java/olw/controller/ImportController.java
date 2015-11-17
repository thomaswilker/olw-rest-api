package olw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestMapping;

import olw.importer.ImporterService;
import olw.model.Material;
import olw.repository.MaterialRepository;

@RepositoryRestController
@RequestMapping("/import")
public class ImportController {

	@Autowired
	ImporterService service;
	
	@Autowired
	MaterialRepository repository;
	
	@RequestMapping("/materials")
	public List<Material> importMaterials() {
		System.out.println("import materials");
		service.importMaterials();
		return repository.findAll(); 
	}
}

package olw.repository.event;

import olw.model.Material;
import olw.repository.index.IndexedMaterialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class MaterialEventHandler {

	@Autowired
	IndexedMaterialRepository repository;
	
	@HandleAfterSave
	public void handleMaterialSave(Material material) {
		System.out.println("event handler material save");
	}
	
}

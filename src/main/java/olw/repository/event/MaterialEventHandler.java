package olw.repository.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import olw.model.AbstractEntity;
import olw.repository.index.IndexedMaterialRepository;

@Component
@RepositoryEventHandler
public class MaterialEventHandler {

	@Autowired
	IndexedMaterialRepository repository;
	
	@HandleBeforeSave
	//@PreAuthorize("hasRole('ADMIN')")
	public void handleSave(AbstractEntity e) {	
		System.out.println("before save of " + e.getClass());
	}
	
}

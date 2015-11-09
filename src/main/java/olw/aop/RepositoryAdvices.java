package olw.aop;

import java.util.ArrayList;
import java.util.List;

import olw.model.Area;
import olw.model.Collection;
import olw.model.Material;
import olw.model.index.IndexedMaterial;
import olw.repository.IndexedCollectionRepository;
import olw.repository.IndexedMaterialRepository;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.mapping.IndexedCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class RepositoryAdvices {

	@Autowired
	IndexedMaterialRepository materialRepository;
	
	@Autowired
	IndexedCollectionRepository collectionRepository;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;
	
	
	@Around(value="execution(* olw.repository.MaterialRepository.save(..)) && args(material)")
	public Object materialSave(ProceedingJoinPoint pjp, Material material) throws Throwable  {
		
		
		material = (Material) pjp.proceed();
		materialRepository.save(mapper.convertValue(material, IndexedMaterial.class));
		
		return material;
	}
	
	
	
	@Around(value="execution(* olw.repository.CollectionRepository.save(..)) && args(collection)")
	public Object collectionSave(ProceedingJoinPoint pjp, Collection collection) throws Throwable {
		
		collection = (Collection) pjp.proceed();
		collectionRepository.save(mapper.convertValue(collection, IndexedCollection.class));
		
		return collection;
	}
	
	@Around(value="execution(* olw.repository.AreaRepository.save(..)) && args(area)")
	public Object areaSave(ProceedingJoinPoint pjp, Area area) throws Throwable  {
		
		area = (Area) pjp.proceed();
		
		List<IndexedCollection> collections = new ArrayList<>();
		
		for(Collection c : area.getCollections())
			collections.add(mapper.convertValue(c, IndexedCollection.class));
			
		collectionRepository.save(collections);
		return area;
	}
	
}

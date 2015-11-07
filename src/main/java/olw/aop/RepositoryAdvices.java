package olw.aop;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import olw.model.Area;
import olw.model.Collection;
import olw.model.Material;
import olw.model.index.IndexedCollection;
import olw.repository.IndexedCollectionRepository;
import olw.repository.IndexedMaterialRepository;
import olw.service.CollectionToIndexConverter;
import olw.service.MaterialToIndexConverter;

@Aspect
@Component
public class RepositoryAdvices {

	@Autowired
	IndexedMaterialRepository materialRepository;
	
	@Autowired
	IndexedCollectionRepository collectionRepository;
	
	@Autowired
	MaterialToIndexConverter mc;
	
	@Autowired
	CollectionToIndexConverter cc;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	
	
	@Around(value="execution(* olw.repository.MaterialRepository.save(..)) && args(material)")
	public Object materialSave(ProceedingJoinPoint pjp, Material material) throws Throwable  {
		
		material = (Material) pjp.proceed();
		materialRepository.save(mc.convert(material));
		
		return material;
	}
	
	
	
	@Around(value="execution(* olw.repository.CollectionRepository.save(..)) && args(collection)")
	public Object collectionSave(ProceedingJoinPoint pjp, Collection collection) throws Throwable {
		
		collection = (Collection) pjp.proceed();
		collectionRepository.save(cc.convert(collection));
		
		return collection;
	}
	
	@Around(value="execution(* olw.repository.AreaRepository.save(..)) && args(area)")
	public Object areaSave(ProceedingJoinPoint pjp, Area area) throws Throwable  {
		
		area = (Area) pjp.proceed();
		
		List<IndexedCollection> collections = new ArrayList<>();
		
		for(Collection c : area.getCollections())
			collections.add(cc.convert(c));
			
		collectionRepository.save(collections);
		return area;
	}
	
}

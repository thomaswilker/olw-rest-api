package olw.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import olw.model.AbstractEntity;
import olw.model.Area;
import olw.model.Collection;
import olw.model.Material;
import olw.repository.index.IndexedCollectionRepository;
import olw.repository.index.IndexedMaterialRepository;
import olw.service.CollectionToIndexConverter;
import olw.service.IndexService;
import olw.service.MaterialToIndexConverter;

@Aspect
@Component
public class RepositoryAdvices {

	@Autowired
	IndexedMaterialRepository materialRepository;
	
	@Autowired
	IndexedCollectionRepository collectionRepository;
	
	@Autowired
	MaterialToIndexConverter materialConverter;
	
	@Autowired
	CollectionToIndexConverter collectionConverter;
	
	@Autowired
	IndexService indexService;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Around(value="execution(* olw.repository.MaterialRepository.save(..)) && args(material)")
	public Object materialSave(ProceedingJoinPoint pjp, Material material) throws Throwable  {
		
		
		material = (Material) pjp.proceed();
		materialRepository.save(materialConverter.convert(material));
		
		return material;
	}
		
	@Around(value="execution(* olw.repository.CollectionRepository.save(..)) && args(collection)")
	public Object collectionSave(ProceedingJoinPoint pjp, Collection collection) throws Throwable {
		
		collection = (Collection) pjp.proceed();
		collectionRepository.save(collectionConverter.convert(collection));
		
		return collection;
	}
	
	@Around(value="execution(* olw.repository.*.save(..)) && args(abstractEntity)")
	public Object areaSave(ProceedingJoinPoint pjp, AbstractEntity abstractEntity) throws Throwable  {
		
		abstractEntity = (AbstractEntity) pjp.proceed();
		indexService.index(abstractEntity.getClass(), abstractEntity.getId());
		
		return abstractEntity;
	}

	
}

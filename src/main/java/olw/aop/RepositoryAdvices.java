package olw.aop;

import java.util.ArrayList;
import java.util.List;

import olw.model.Area;
import olw.model.Collection;
import olw.model.Material;
import olw.model.index.IndexedCollection;
import olw.repository.index.IndexedCollectionRepository;
import olw.repository.index.IndexedMaterialRepository;
import olw.service.CollectionToIndexConverter;
import olw.service.MaterialToIndexConverter;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	ElasticsearchTemplate template;
	
	@Autowired
	CollectionToIndexConverter collectionConverter;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;
	
	
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
	
	@Around(value="execution(* olw.repository.AreaRepository.save(..)) && args(area)")
	public Object areaSave(ProceedingJoinPoint pjp, Area area) throws Throwable  {
		
		area = (Area) pjp.proceed();
		
		List<IndexedCollection> collections = new ArrayList<>();
		
		for(Collection c : area.getCollections()) {
			collections.add(collectionConverter.convert(c));
		}
			
			
		collectionRepository.delete(collections);
		collectionRepository.save(collections);
		//template.bulkUpdate(collections);
		
		
		return area;
	}
	
}

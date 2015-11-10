package olw.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import olw.model.AbstractEntity;
import olw.model.annotations.ContainedIn;
import olw.model.annotations.IndexedBy;
import olw.model.index.IndexedEntity;

@Service
@EnableAsync
public class IndexService {

	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	WebApplicationContext context;
	
	private Repositories repositories;

	@Autowired
	@Qualifier("defaultConversionService")
	ConversionService conversionService;
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Async
	@Transactional
	public <T extends AbstractEntity> void index(Class<T> entityClass, Long id) {
    	
		// retrieve all registered repositories 
		repositories = new Repositories(context);
		
		// initialize entity
		T entity = entityManager.find(entityClass, id);
    	
    	// Get all declared Fields
    	Field[] fields = entity.getClass().getDeclaredFields();
    	
    	// iterate only annotated fields an run indexation
    	Arrays.stream(fields)
    		  .filter(f -> f.isAnnotationPresent(ContainedIn.class))
    		  .forEach(f -> run(f,entity));
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> void run(Field f, T a) {
		
		BeanWrapper wrapper =new BeanWrapperImpl(a);
		
		try { 
			
			Object o = wrapper.getPropertyValue(f.getName());
			
			if(o instanceof Collection) {
				
				Collection<? extends AbstractEntity> c = (Collection<? extends AbstractEntity>) o;
				Class<? extends AbstractEntity> type = (Class<? extends AbstractEntity>) GenericCollectionTypeResolver.getCollectionFieldType(f);
				
				if(AbstractEntity.class.isAssignableFrom(type) && type.isAnnotationPresent(IndexedBy.class)) {
					Class<? extends IndexedEntity> indexedBy = type.getAnnotation(IndexedBy.class).value();
					List<? extends IndexedEntity> indexedElements = c.stream().map(ce -> conversionService.convert(ce, indexedBy)).collect(Collectors.toList());
					ElasticsearchCrudRepository repository = (ElasticsearchCrudRepository) repositories.getRepositoryFor(indexedBy);
			    	repository.delete(indexedElements);
					repository.save(indexedElements);
				}
				
			}
		} catch(Exception e) {
			logger.info("error occured: " + e.getMessage());
		};
		
	}
	
	
}

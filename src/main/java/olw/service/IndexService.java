package olw.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
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
	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity> void indexAll(Collection<T> entities) {
		
		entities.forEach(e -> index(e));
		
	}
	
	@Async
	@Transactional
	public <T extends AbstractEntity> void indexOne(T e) {
		index(e);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public <T extends AbstractEntity> void index(T e) {
    	
		//logger.info(String.format("index entity %s with id %s", e.getClass(), e.getId()));
		
		// all registered repositories 
		repositories = new Repositories(context);
		
		// initialize entity
		JpaRepository<T,Long> repository = (JpaRepository<T, Long>) repositories.getRepositoryFor(e.getClass());
		T entity = repository.findOne(e.getId());
		
		if(entity != null) {
			
			// Index entity if class is annotated with @IndexedBy
			if(entity.getClass().isAnnotationPresent(IndexedBy.class)) {
				
				Class<? extends IndexedEntity> targetClass = entity.getClass().getAnnotation(IndexedBy.class).value();
				ElasticsearchCrudRepository<IndexedEntity, Long> repo = (ElasticsearchCrudRepository<IndexedEntity, Long>) repositories.getRepositoryFor(targetClass);
				
				// a converter must be provided
				if(conversionService.canConvert(entity.getClass(), targetClass)) {
					repo.save(conversionService.convert(entity, targetClass));
				} else {
					logger.info(String.format("No converter provided for conversion from %s to %s", entity.getClass().getName(), targetClass.getName()));
				}
			}
			
	    	// Get all declared Fields
	    	Field[] fields = entity.getClass().getDeclaredFields();
	    	
	    	// iterate fields, filter by @ContainedIn, run indexation
	    	Arrays.stream(fields)
	    		  .filter(f -> f.isAnnotationPresent(ContainedIn.class))
	    		  .forEach(f -> run(f,entity));
		} else {
			System.out.println(String.format("can't find entity of class of class %s with id %s", e.getClass(), e.getId()));
		}
		
    }
	
	
	@SuppressWarnings({ "unchecked"})
	private <T> void run(Field f, T a) {
		
		BeanWrapper wrapper =new BeanWrapperImpl(a);
		
		try { 
			
			Object o = wrapper.getPropertyValue(f.getName());
			
			if(o instanceof Collection) {
				
				Collection<? extends AbstractEntity> c = (Collection<? extends AbstractEntity>) o;
				Class<? extends AbstractEntity> type = (Class<? extends AbstractEntity>) GenericCollectionTypeResolver.getCollectionFieldType(f);
				
				if(AbstractEntity.class.isAssignableFrom(type) && type.isAnnotationPresent(IndexedBy.class)) {
					c.stream().forEach(ce -> index(ce));
				}
			} else if(o instanceof AbstractEntity && o.getClass().isAnnotationPresent(IndexedBy.class)) {
				AbstractEntity e = (AbstractEntity) o; 
				index(e);
			}
			
		} catch(Exception e) {
			logger.info(String.format("exception of type %s. message: %s", e.getClass(), e.getMessage()));
		};
		
	}
	
	
}

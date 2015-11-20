package olw.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
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
import olw.model.annotations.IndexEmbedded;
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
	
	
	@PostConstruct
	public void postConstruct() {
		repositories = new Repositories(context);
	}
	
	@Async
	@Transactional
	public <T extends AbstractEntity> void indexAll(Collection<T> entities) {
		entities.forEach(e -> writeToIndexRecursivly(e));
	}
	
	@Async
	@Transactional
	public <T extends AbstractEntity> void indexOne(T e) {
		writeToIndexRecursivly(e);
	}
	
	
	public <T extends AbstractEntity> void writeToIndexRecursivly(T e) {
    	
		logger.info(String.format("index entity %s with id %s", e.getClass(), e.getId()));
		T entity = merge(e);
		
		writeToIndex(entity);
		getAnnotatedFields(entity.getClass(), ContainedIn.class).forEach(f -> run(f, entity, this::writeToIndexRecursivly));
		List<Field> embedded = getAnnotatedFields(entity.getClass(), IndexEmbedded.class);
		logger.info("embedded fields: " + embedded.size());
		embedded.forEach(f -> run(f, entity, this::writeToIndex));
	    	
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Index entity if class is annotated with @IndexedBy and a converter is registered 
	 * @param entity
	 */
	private <T extends AbstractEntity> void writeToIndex(T entity) {
		
		logger.info("write to index " + entity.getClass() + " with id " + entity.getId());
		if(isIndexable(entity)) {
				
			Class<? extends IndexedEntity> targetClass = entity.getClass().getAnnotation(IndexedBy.class).value();
			ElasticsearchCrudRepository<IndexedEntity, Long> repo = (ElasticsearchCrudRepository<IndexedEntity, Long>) repositories.getRepositoryFor(targetClass);
				
			if(conversionService.canConvert(entity.getClass(), targetClass)) {
				repo.save(conversionService.convert(entity, targetClass));
			} else {
				logger.info(String.format("No converter provided for conversion from %s to %s", entity.getClass().getName(), targetClass.getName()));
			}
		}
	}
	
	@SuppressWarnings({ "unchecked"})
	private <T extends AbstractEntity> void run(Field f, Object entity, Consumer<T> indexFunction) {
		
		BeanWrapper wrapper = new BeanWrapperImpl(entity);
		
		try { 
			
			Object o = wrapper.getPropertyValue(f.getName());
			
			if(o instanceof Collection) {
				
				Collection<?> c = (Collection<?>) o;
				Class<?> type = (Class<?>) GenericCollectionTypeResolver.getCollectionFieldType(f);
				
				if(AbstractEntity.class.isAssignableFrom(type) && type.isAnnotationPresent(IndexedBy.class)) {
					c.stream().forEach(ce -> indexFunction.accept((T) ce));
				}
			} else if(o instanceof AbstractEntity && isIndexable((T) o)) {
				indexFunction.accept((T) o);
			}
			
		} catch(Exception e) {
			logger.info(String.format("exception of type %s. message: %s", e.getClass(), e.getMessage()));
		};
		
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractEntity> T merge(T entity) {
		
		JpaRepository<T,Long> repository = (JpaRepository<T, Long>) repositories.getRepositoryFor(entity.getClass());
		return repository.findOne(entity.getId());
	}

	
	private <T extends AbstractEntity> boolean isIndexable(T entity) {
		return entity.getClass().isAnnotationPresent(IndexedBy.class);
	}
	
	/**
	 * Get all declared Fields of baseClass and filter all fields where annotation is present
	 * @param baseClass
	 * @param annotation
	 * @return
	 */
	private List<Field> getAnnotatedFields(Class<? extends AbstractEntity> baseClass, Class<? extends Annotation> annotation) {
	
		return Arrays.stream(baseClass.getDeclaredFields()).filter(f -> f.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }
	
}

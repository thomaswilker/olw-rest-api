package olw.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

import olw.model.AbstractEntity;
import olw.model.annotations.ContainedIn;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class IndexService {

	@Autowired
	ThreadPoolTaskExecutor executor;
	
	@PersistenceUnit
	EntityManagerFactory entityManagerFactory;
	
	@Autowired
	WebApplicationContext context;
	
	@Autowired
	@Qualifier("defaultConversionService")
	ConversionService conversionService;
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Transactional
	public <S,T> void index(Class<T> entityClass, Long id) {
    	
		logger.info("index elements");

		executor.execute(() -> {
		
			EntityManager em = entityManagerFactory.createEntityManager();
			Repositories repositories = new Repositories(context);
			Session session = em.unwrap(Session.class);
			Transaction tx = session.beginTransaction();
	    	T a = em.find(entityClass, id);
	    	logger.info("log elements");
	    	
			Arrays.stream(a.getClass().getDeclaredFields())
					.filter(x -> x.isAnnotationPresent(ContainedIn.class))
					.forEach(f -> {
						try { 
							logger.info("field: " + f.getName());
					    	
							BeanWrapper wrapper =new BeanWrapperImpl(a);
							Object o = wrapper.getPropertyValue(f.getName());
							if(o instanceof java.util.Collection) {
								
								logger.info("is of type collection");
								
								Collection<? extends AbstractEntity> c = (Collection<? extends AbstractEntity>) o;
								Class indexClass = c.getClass().getGenericInterfaces()[0].getClass();
								ElasticsearchCrudRepository repository = (ElasticsearchCrudRepository) repositories.getRepositoryFor(indexClass);
						    	
								List indexedElements = c.stream().filter(ce -> conversionService.canConvert(ce.getClass(), indexClass))
														    .map(ce -> conversionService.convert(ce, indexClass))
														    .collect(Collectors.toList());
								
								logger.info("number of elements to reindex " + indexedElements.size());
								
								repository.delete(indexedElements);
								repository.save(indexedElements);
								
							}
						} catch(Exception e) {
							logger.info("error occurred:" + e.getMessage());
						};
					});
	    	
			tx.commit();
		});
	}
	
	
}

package olw.aop;

import java.util.Collection;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import olw.model.AbstractEntity;
import olw.service.IndexService;

@Aspect
@Component
public class RepositoryAdvices {

	@Autowired
	IndexService indexService;
	
	@Around(value="execution(* olw.repository.*.save(..)) && args(o)")
	public Object entitySave(ProceedingJoinPoint pjp, AbstractEntity o) throws Throwable  {
		
		o = (AbstractEntity) pjp.proceed();
		indexService.indexOne(o);
		return o;
	}
	
	@Around(value="execution(* olw.repository.*.save(..)) && args(o)")
	public Object entitiesSave(ProceedingJoinPoint pjp, Iterable<? extends AbstractEntity> o) throws Throwable  {
		
		Collection<? extends AbstractEntity> entities = (Collection<? extends AbstractEntity>) pjp.proceed();
		indexService.indexAll(entities);
		return entities;
	}

	
	
}

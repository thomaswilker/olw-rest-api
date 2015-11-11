package olw.aop;

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
	
	@Around(value="execution(* olw.repository.*.save(..)) && args(abstractEntity)")
	public Object entitySave(ProceedingJoinPoint pjp, AbstractEntity abstractEntity) throws Throwable  {
		
		abstractEntity = (AbstractEntity) pjp.proceed();
		indexService.index(abstractEntity.getClass(), abstractEntity.getId());
		
		return abstractEntity;
	}

	
	
	
}

package olw.aop;

import org.apache.log4j.Logger;
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
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Around(value="execution(* olw.repository.*.save(..)) && args(abstractEntity)")
	public Object areaSave(ProceedingJoinPoint pjp, AbstractEntity abstractEntity) throws Throwable  {
		
		abstractEntity = (AbstractEntity) pjp.proceed();
		indexService.index(abstractEntity.getClass(), abstractEntity.getId());
		
		return abstractEntity;
	}

	
}

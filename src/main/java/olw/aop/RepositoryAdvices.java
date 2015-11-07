package olw.aop;

import olw.model.Material;
import olw.model.index.IndexedMaterial;
import olw.repository.IndexedMaterialRepository;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryAdvices {

	@Autowired
	IndexedMaterialRepository materialIndex;
	
	@Autowired
	Converter<Material, IndexedMaterial> mc;
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Around(value="execution(* olw.repository.MaterialRepository.save(..)) && args(material)")
	public Object materialSave(ProceedingJoinPoint pjp, Material material) throws Throwable {
		
		material = (Material) pjp.proceed();
		logger.info(String.format("index material with id ", material.getId()));
		materialIndex.save(mc.convert(material));
		logger.info("material indexed successfully");
		
		return material;
	}
	
}

//package olw.security;
//
//import java.io.Serializable;
//
//import org.apache.log4j.Logger;
//import org.springframework.security.access.PermissionEvaluator;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class OlwPermissionEvaluator implements PermissionEvaluator {
//
//	Logger logger = Logger.getLogger(this.getClass());
//	
//	
//	@Override
//	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//		
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			String auth = mapper.writeValueAsString(authentication);
//			String target = mapper.writeValueAsString(targetDomainObject);
//			
//			logger.info(auth);
//			logger.info(target);
//			logger.info(permission);
//			
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		return true;
//	}
//
//	@Override
//	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
//			Object permission) {
//		
//		System.out.println("long: " +  authentication.getName());
//		logger.info(targetId);
//		logger.info(targetType);
//		logger.info(permission);
//		return true;
//	}
//
//}

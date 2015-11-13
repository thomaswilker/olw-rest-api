package olw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import olw.security.OlwPermissionEvaluator;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Autowired
	OlwPermissionEvaluator olwPermissionEvaluator;
	
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
		//handler.setPermissionEvaluator(olwPermissionEvaluator);
		//System.out.println("test");
		return handler;
	}
	
}

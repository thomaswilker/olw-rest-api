//package olw;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	UserDetailsService userDetailsService;
//		
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        
//		http.formLogin().and()
//		  .csrf().disable()
//	      .userDetailsService(userDetailsService)
//	      .rememberMe().disable()
//	      .sessionManagement().sessionFixation().newSession();
////	      .and().authorizeRequests()
////	        .antMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN","LECTURER","ASSISTANT")
////	        .antMatchers(HttpMethod.PUT, "/**").hasAnyRole("ADMIN","LECTURER","ASSISTANT")
////	        .antMatchers(HttpMethod.PATCH, "/**").hasAnyRole("ADMIN","LECTURER","ASSISTANT");
//	      
//    }
//
//    
//}

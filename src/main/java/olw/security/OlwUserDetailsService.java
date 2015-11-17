//package olw.security;
//
//import java.util.HashSet;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import olw.model.Lecturer;
//import olw.repository.AbstractUserRepository;
//
//@Service
//public class OlwUserDetailsService implements UserDetailsService {
//
//	@Autowired
//	AbstractUserRepository repository; 
//	
//	@Override
//	public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
//		
//		List<GrantedAuthority> authorities = null;
//		//AbstractUser user = repository.findByAccount(account);
//		authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
//		
//		System.out.println(account);
//		return new OlwUserDetails(new Lecturer(1l, "admin", "admin"), new HashSet<Lecturer>(), authorities);
//	}
//
//}

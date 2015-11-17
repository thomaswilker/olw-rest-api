//package olw.security;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import olw.model.User;
//import olw.model.Lecturer;
//
//public class OlwUserDetails implements UserDetails  {
//
//	private static final long serialVersionUID = 4564617510886927733L;
//	Set<Lecturer> permissions = new HashSet<>();
//	private User user;
//	private Collection<? extends GrantedAuthority> authorities;
//	
//	public OlwUserDetails(User user, Set<Lecturer> permissionsForUsers, List<? extends GrantedAuthority> authorities) {
//		this.user = user;
//		this.permissions = permissionsForUsers;
//		this.authorities = authorities;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return authorities;
//	}
//
//	@Override
//	public String getPassword() {
//		// TODO Auto-generated method stub
//		return "";
//	}
//
//	@Override
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return String.format("%s %s", user.getFirstName(), user.getLastName());
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//}

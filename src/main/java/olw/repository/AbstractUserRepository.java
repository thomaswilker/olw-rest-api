package olw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import olw.model.User;

public interface AbstractUserRepository extends JpaRepository<User, Long> {

	public User findByAccount(String account);
		
}

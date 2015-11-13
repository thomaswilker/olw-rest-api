package olw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.access.prepost.PreAuthorize;

@NoRepositoryBean
public interface SecuredRepository<T> extends JpaRepository<T, Long> {

	@Override
	<S extends T> S save(S entity);
	
	@Override
	void delete(Long id);

	@Override
	void delete(T entity);

	@Override
	void delete(Iterable<? extends T> entities);

	@Override
	void deleteAll();
	
}

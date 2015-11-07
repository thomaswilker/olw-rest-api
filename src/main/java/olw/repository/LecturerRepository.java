package olw.repository;

import java.util.List;

import olw.model.Assistant;
import olw.model.Lecturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

	@RestResource(path="assistant", rel="assistant")
	List<Assistant> findByAssistants(@Param("q") Assistant a);
	
}

package olw.repository;

import java.util.List;

import olw.model.Assistant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface AssistantRepository extends JpaRepository<Assistant, Long> {

	
}

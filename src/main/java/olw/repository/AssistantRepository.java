package olw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import olw.model.Assistant;

public interface AssistantRepository extends JpaRepository<Assistant, Long> {

	
}

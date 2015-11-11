package olw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import olw.model.License;
import olw.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
	
	@RestResource(path="license", rel="license")
	public List<Material> findByLicense(@Param("q") License license);
	
	@RestResource(path="name", rel="name")
	public List<Material> findByNameIgnoreCaseContaining(@Param("q") String name);
	
	@RestResource(path="description", rel="description")
	public List<Material> findByDescriptionIgnoreCaseContaining(@Param("q") String description);
	
	@RestResource(path="open", rel="open")
	public List<Material> findByOpen(@Param("q") Boolean open);
	
}

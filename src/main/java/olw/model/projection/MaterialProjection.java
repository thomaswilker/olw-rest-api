package olw.model.projection;

import java.util.List;

import olw.model.Area;
import olw.model.Material;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="DefaultMaterial", types=Material.class)
public interface MaterialProjection {
	
	Long getId();
	String getTitle();
	String getDescription();
	List<Area> getAreas();
}

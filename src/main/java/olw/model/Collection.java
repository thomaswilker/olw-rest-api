package olw.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class Collection extends AbstractEntity {

	private String name = null;
	private String note = null;
	private String description = null;
	private Date date = null;
	private Boolean newest = false;
	private Boolean deleted = null;
	
	@NotNull 
	@Size(min=1)
	@ManyToMany(cascade=CascadeType.ALL)
	private List<Lecturer> lecturers = new ArrayList<>();
	
	@ElementCollection
	private Set<Tag> tags = new LinkedHashSet<>();
	
	@NotNull 
	@Size(min=1)
	@ManyToMany(cascade=CascadeType.ALL)
	private List<Area> areas = new ArrayList<>();
	
	@NotNull 
	@ManyToMany(cascade=CascadeType.ALL)
	protected List<Material> materials = new ArrayList<>();
	
	
	//protected Set<Semester> semesters = null;
	//protected List<ExternalResource> externalResources = null;
	//protected CollectionType type = null;
	
	
}

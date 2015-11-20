package olw.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import olw.model.annotations.IndexEmbedded;
import olw.model.annotations.IndexedBy;
import olw.model.index.IndexedCollection;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@IndexedBy(IndexedCollection.class)
public class Collection extends AbstractEntity {

	@NotNull
	private String name = null;
	
	private Date date = null;
	private Boolean newest = false;
	private Boolean deleted = null;
	private Long oldId;

	@Size(max=1000)
	private String note = null;
	
	@Size(max=3000)
	private String description = null;
	
	@NotNull 
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Lecturer> lecturers = new LinkedHashSet<>();
	
	@ElementCollection
	private Set<String> tags = new LinkedHashSet<>();
	
	@NotNull 
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Area> areas = new LinkedHashSet<>();
	
	@NotNull 
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Semester> semesters = new LinkedHashSet<>();
	
	@NotNull
	@IndexEmbedded
	@ManyToMany(cascade=CascadeType.ALL)
	protected List<Material> materials = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name = "newerVersion", nullable = true)
	Collection newestVersion; 
	
	@OneToMany
	@JoinColumn(name="newerVersion")
	List<Collection> olderVersions;
	
}

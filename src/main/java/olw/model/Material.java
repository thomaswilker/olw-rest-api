package olw.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import olw.model.annotations.ContainedIn;
import olw.model.annotations.IndexedBy;
import olw.model.index.IndexedMaterial;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@IndexedBy(IndexedMaterial.class)
public class Material extends AbstractEntity {
	
	private String uuid = UUID.randomUUID().toString();
	private String name;
	private String note;
	private String description;
	
	private Boolean open = false;
	private Boolean deleted = false;
	private Boolean downloadable = false;
	
	private Date date = new Date();
	private Integer visits = 0;
	
	@ElementCollection
	private Set<String> tags = new LinkedHashSet<>();
	
	@NotNull
	@ManyToOne(cascade=CascadeType.ALL)
	private License license;
	
	@NotNull 
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Language> languages = new LinkedHashSet<>();
	
	@NotNull 
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Lecturer> lecturers = new LinkedHashSet<>();
	
	@JsonIgnore
	@NotNull
	@ManyToMany(mappedBy="materials")
	@ContainedIn
	private List<Collection> collections = new ArrayList<>();
	
	
	
	public Material(Long id) {
		super(id);
	}
	
	public Material(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	
	
}

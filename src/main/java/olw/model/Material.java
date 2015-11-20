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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import olw.model.annotations.ContainedIn;
import olw.model.annotations.IndexedBy;
import olw.model.index.IndexedMaterial;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@IndexedBy(IndexedMaterial.class)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Material extends AbstractEntity {
	
	private String uuid = UUID.randomUUID().toString();
	private String name;
	private String note;
	private Long oldId;
	
	@Size(max=1000)
	private String description;
	
	private Boolean open = false;
	private Boolean deleted = false;
	private Boolean downloadable = false;
	
	private Date date = new Date();
	private Integer visits = 0;
	
	@ElementCollection
	private Set<String> tags = new LinkedHashSet<>();
	
	@NotNull
	@ManyToOne
	private License license;
	
	@NotNull 
	@ManyToMany
	private Set<Language> languages = new LinkedHashSet<>();
	
	@NotNull 
	@ManyToMany
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

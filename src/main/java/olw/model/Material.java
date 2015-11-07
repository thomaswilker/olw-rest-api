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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
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
	private Set<Tag> tags = new LinkedHashSet<>();
	
	@NotNull
	@ManyToOne(cascade=CascadeType.ALL)
	private License license;
	
	@NotNull 
	@Size(min=1)
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Language> languages = new LinkedHashSet<>();
	
	@NotNull 
	@Size(min=1)
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Lecturer> lecturer = new LinkedHashSet<>();
	
	
	public Material(Long id) {
		super(id);
	}
	
	public Material(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	// protected ResourceCharacteristic characteristic = new ResourceCharacteristic(2l);
	// private List<ExternalResource> externalResources = null;
	// private List<User> users = null;
	// private List<URL> links = null;
	
}

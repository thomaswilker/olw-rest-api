package olw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import olw.model.annotations.ContainedIn;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@DiscriminatorValue("LE")
public class Lecturer extends User {

	private String title;
	private String organization;
	
	@Size(max=1000)
	private String about;
	private String website;
	
	@JsonIgnore
	protected Photo photo = null;
	
	public Lecturer(Long id) {
		super(id);
	}
	
	public Lecturer(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}
	
	public Lecturer(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	private Set<Assistant> assistants = new HashSet<>();
	
	@JsonIgnore
	@ContainedIn
	@ManyToMany(mappedBy="lecturers")
	private List<Material> materials = new ArrayList<>();
	
	@JsonIgnore
	@ContainedIn
	@ManyToMany(mappedBy="lecturers")
	private List<Collection> collections = new ArrayList<>();
	
	@Embeddable
	@Data
	public static class Photo {
		
		@Basic(fetch=FetchType.LAZY)
		private String type = null;
		
		@Lob
		@Basic(fetch=FetchType.LAZY)
		private byte[] data = null;
		
	}
	
}

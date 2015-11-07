package olw.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Lecturer extends AbstractUser {

	private String organization;
	private String about;
	private String website;
	
	@JsonIgnore
	protected Photo photo = null;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	private Set<Assistant> assistants = new HashSet<>();
	
	public Lecturer(Long id) {
		super(id);
	}
	
	public Lecturer(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}
	
	public Lecturer(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
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

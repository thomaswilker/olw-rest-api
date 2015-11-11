package olw.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@DiscriminatorValue("AS")
public class Assistant extends User {
	
	public Assistant(Long id) {	super(id); }
	
	public Assistant(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	public Assistant(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}
	
}

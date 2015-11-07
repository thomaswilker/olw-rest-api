package olw.model;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class Assistant extends AbstractUser {
	
	public Assistant(Long id) {	super(id); }
	
	public Assistant(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
}

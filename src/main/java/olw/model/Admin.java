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
@DiscriminatorValue("AD")
public class Admin extends User {
	
	public Admin(Long id) {	super(id); }
	
	public Admin(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	public Admin(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
	}
	
}

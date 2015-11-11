package olw.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@DiscriminatorColumn(name="Role")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class User extends AbstractEntity {

	private String firstName;
	private String lastName;
	private String email;
	
	@JsonIgnore
	private String account;
	
	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public User(Long id, String firstName, String lastName) {
		this(firstName,lastName);
		this.id = id;
	}
	
	public User(Long id) {
		super(id);
	}
	
}

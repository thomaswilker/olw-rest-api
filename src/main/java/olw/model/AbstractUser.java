package olw.model;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractUser extends AbstractEntity {

	private String firstName;
	private String lastName;
	
	@JsonIgnore
	private String account;
	
	public AbstractUser(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public AbstractUser(Long id) {
		super(id);
	}
	
}

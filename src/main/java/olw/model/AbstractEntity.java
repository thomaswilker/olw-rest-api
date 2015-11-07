package olw.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.hateoas.Identifiable;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity implements Identifiable<Long> {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	@Version
	protected Long version;
	
	public AbstractEntity(Long id) {
		this.id = id;
	}
	
}

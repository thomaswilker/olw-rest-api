package olw.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.hateoas.Identifiable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity implements Identifiable<Long> {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NonNull
	protected Long id;
	
	@Version
	protected Long version;
	
	public AbstractEntity(Long id) {
		this.id = id;
	}
	
}

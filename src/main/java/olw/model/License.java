package olw.model;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class License extends AbstractEntity {

	private String name;
	private String note;
	
	public License(Long id, String name) {
		super(id);
		this.name = name;
	}
	
	
	
}

package olw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Area extends AbstractEntity {

	@NonNull
	private String name;
	
	public Area(Long id, String name) {
		super(id);
		this.name = name;
	}

	
	@JsonIgnore
	@ManyToMany(mappedBy="areas")
	private List<Collection> collections = new ArrayList<>();
	
	
}

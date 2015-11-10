package olw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import olw.model.annotations.ContainedIn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@ContainedIn
	@ManyToMany(mappedBy="areas")
	private List<Collection> collections = new ArrayList<>();
	
	
}

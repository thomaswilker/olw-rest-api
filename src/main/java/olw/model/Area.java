package olw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import olw.model.annotations.ContainedIn;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Area extends AbstractEntity {

	@NonNull
	private String name;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Section section;
	
	public Area(Long id, String name, Section section) {
		super(id);
		this.name = name;
		this.section = section;
	}

	
	@JsonIgnore
	@ContainedIn
	@ManyToMany(mappedBy="areas")
	private List<Collection> collections = new ArrayList<>();
	
	
	
}

package olw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

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
public class Section extends AbstractEntity {
	
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy="section")
	private List<Area> areas = new ArrayList<>();
	
	
	public Section(Long id) {
		super(id);
	}
	
	public Section(Long id, String name) {
		super(id);
		this.name = name;
	}
	
}

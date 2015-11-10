package olw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import olw.model.annotations.ContainedIn;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@RequiredArgsConstructor
@NoArgsConstructor
public class Language extends AbstractEntity {

	@NonNull
	private String name;
	
	public Language(Long id, String name) {
		super(id);
		this.name = name;
	}

	@JsonIgnore
	@ManyToMany(mappedBy="languages")
	@ContainedIn
	private List<Material> materials = new ArrayList<>();
	
}

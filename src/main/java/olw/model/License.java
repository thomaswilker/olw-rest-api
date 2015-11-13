package olw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import olw.model.annotations.ContainedIn;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class License extends AbstractEntity {

	private String name;
	private String note;
	
	public License(Long id, String name) {
		super(id);
		this.name = name;
	}
	
	@JsonIgnore
	@ContainedIn
	@OneToMany(mappedBy="license")
	private List<Material> materials = new ArrayList<>();
	
}

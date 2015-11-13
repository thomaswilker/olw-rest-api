package olw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
public class Semester extends AbstractEntity {

	public enum Part {
		None, SoSe, WiSe
	}
	
	@NotNull @NonNull
	@Min(2000) @Max(2050)
	protected Integer year;
	
	@NotNull @NonNull
	@Enumerated(EnumType.STRING)
	protected Part part;
	
	@JsonIgnore
	@ContainedIn
	@ManyToMany(mappedBy="semesters")
	private List<Collection> collections = new ArrayList<>();
	
	public Semester(Long id, Integer year, Part part) {
		super(id);
		this.year = year;
		this.part = part;
	}
	
	@Transient
	public String getName() {
		return part.toString() + " " + year;
	}
	
}

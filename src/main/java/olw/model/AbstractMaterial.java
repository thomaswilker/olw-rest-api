package olw.model;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
abstract public class AbstractMaterial extends AbstractEntity {

	private String uuid = UUID.randomUUID().toString();
	private String name;
	private String note;
	private String description;
	
	private Boolean open = false;
	private Boolean deleted = false;
	private Boolean downloadable = false;
	
	private Date date = new Date();
	private Integer visits = 0;
	
	public AbstractMaterial(Long id) {
		super(id);
	}
	
	public AbstractMaterial(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
}

package olw.model.index;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import olw.model.Area;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Material;
import olw.model.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@Getter
@Setter
@Document(indexName = "materials", type = "material" , shards = 1, replicas = 1, indexStoreType = "fs", refreshInterval = "1")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class IndexedMaterial {

	@Id
	private Long id;
	
	private String name;
	private String description;
	private Long version;
	private String uuid;
	private String note;
	
	private Boolean open = false;
	private Boolean deleted = false;
	protected Boolean downloadable = false;
	
	protected Date date = new Date();
	protected Integer visits = 0;
	
	private List<Area> areas;
	private List<Language> languages;
	private List<Tag> tags;
	private List<Lecturer> lecturer;
	
	private License license;
	
}

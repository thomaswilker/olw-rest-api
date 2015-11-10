package olw.model.index;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import olw.model.Area;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;

@Getter
@Setter
@Document(indexName = "materials", type = "material" , shards = 1, replicas = 1, indexStoreType = "fs", refreshInterval = "1")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class IndexedMaterial extends IndexedEntity {

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
	
	private List<Language> languages;
	private List<String> tags;
	private List<Lecturer> lecturer;
	
	private License license;
	
}

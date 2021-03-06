package olw.model.index;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import olw.model.Area;
import olw.model.Lecturer;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@Document(indexName = "collections", type = "collection" , shards = 1, replicas = 1, indexStoreType = "fs", refreshInterval = "1")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class IndexedCollection extends IndexedEntity {

	@Id
	private Long id;
	
	private String name;
	private String note;
	private String description;
	private Date date;
	private Boolean newest = false;
	private Boolean deleted = false;
	 
	private Set<Lecturer> lecturers = new LinkedHashSet<>();
	private Set<String> tags = new LinkedHashSet<>();
	private Set<Area> areas = new LinkedHashSet<>();
	protected List<IndexedMaterial> materials = new ArrayList<>();
	
}

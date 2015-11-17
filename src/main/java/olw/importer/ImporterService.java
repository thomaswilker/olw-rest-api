package olw.importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import olw.model.AbstractEntity;
import olw.model.Area;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Material;
import olw.model.Semester;
import olw.repository.LecturerRepository;
import olw.repository.LicenseRepository;
import olw.repository.MaterialRepository;
import olw.repository.index.IndexedMaterialRepository;

@Service
public class ImporterService {

	@Autowired
	ConverterService converterService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	private final RestTemplate template = new RestTemplate();
	private final String api = "https://www.openlearnware.de/olw-rest-db/api/";
	private final Logger logger = Logger.getLogger(this.getClass());
	
	private final Map<Class<?>, String> restResource = new HashMap<>();
	private final Map<Class<?>, String> restCollection = new HashMap<>();
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	LecturerRepository lecturerRepository;
	
	@Autowired
	LicenseRepository licenseRepository;
	
	
	@Autowired
	IndexedMaterialRepository indexedMaterialRepository;
	
	@Autowired
	private ApplicationContext context;
	
	private Repositories repositories;
	
	@Autowired
	EntityManager em;
	
	
	@PostConstruct
	public void postConstruct() {
		 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// Retrieve all Spring data repositories
		repositories = new Repositories(context);
		 
		// Add urls for single rest resources
		restCollection.put(Material.class, "resource-overview/filter/index/all");
		 
		// Add urls for multi rest resources
		restCollection.put(License.class, "license/all");
		restCollection.put(Lecturer.class, "user/all");
		restCollection.put(Language.class, "language/all");
		restCollection.put(Area.class, "area/all");
		restCollection.put(Semester.class, "semester/all");
		
	}
	
	private <T extends AbstractEntity> JpaRepository<T, Long> getRepository(Class<T> forClass) {
		return (JpaRepository<T, Long>) repositories.getRepositoryFor(forClass);
	}
	
	
	public <T extends AbstractEntity> Map<Long, Long> importEntity(Class<T> forClass) throws Exception {
	
		String url = restCollection.get(forClass);
		Map<Long, Long> result = new HashMap<>();
		
		if(url != null) {
			JpaRepository<T, Long> repository = getRepository(forClass); 
			repository.deleteAll();
			ArrayNode array = find(url).asArray();
			List<T> list = converterService.convert(array, forClass, ArrayList::new);
			
			result = list.stream().collect(Collectors.toMap(e -> e.getId(), e -> { repository.save(e); return e.getId();}));
		}
		
		return result;
	}
	
	public void importMaterials() {
		
		try {
			
			indexedMaterialRepository.deleteAll();
			materialRepository.deleteAll();
			lecturerRepository.deleteAll();
			licenseRepository.deleteAll();
			
			Map<Long, Long> lecturersMap = importEntity(Lecturer.class);
			Map<Long, Long> licensesMap = importEntity(License.class);
			Map<Long, Long> languagesMap = importEntity(Language.class);
			
			converterService.setLecturers(lecturersMap);
			converterService.setLicenses(licensesMap);
			converterService.setLanguages(languagesMap);
			
			String materialUrl = restCollection.get(Material.class);
			List<JsonNode> list = StreamSupport.stream(find(materialUrl).asArray().spliterator(), false)
															  .collect(Collectors.toList());
			
			List<Material> materials = converterService.convert(list, Material.class, ArrayList::new);
			materialRepository.save(materials);
			
			
		} catch(Exception e) {
			logger.info(e.getMessage());
		}
	}
	
	private AjaxResult findOne(Integer id, String... pathSegments) {
		pathSegments = (String[]) ArrayUtils.add(pathSegments, id.toString());
		return find(pathSegments);
	}
	
	private AjaxResult find(String... pathSegments) {
		return new AjaxResult(template.getForObject(select(pathSegments).toUriString(), String.class));
	}
	
	private UriComponentsBuilder select(String... path) {
		List<String> segments = Arrays.asList(path).stream().flatMap(s -> Stream.of(s.split("/"))).collect(Collectors.toList());
		return UriComponentsBuilder.fromHttpUrl(api).pathSegment(segments.toArray(new String[]{}));
	}
	
}

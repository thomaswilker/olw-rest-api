package olw.importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.scheduling.annotation.Async;
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
	private ApplicationContext context;
	
	private Repositories repositories;
	
	
	@PostConstruct
	public void postConstruct() {
		 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// Retrieve all Spring data repositories
		repositories = new Repositories(context);
		 
		// Add urls for single rest resources
		restResource.put(Material.class, "resource-detailview/index");
		 
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
	
	@Transactional
	public <T extends AbstractEntity> void importEntity(Class<T> forClass) throws Exception {
	
		String url = restCollection.get(forClass);
		
		if(url != null) {
			JpaRepository<T, Long> repository = getRepository(forClass); 
			repository.deleteAll();
			ArrayNode array = find(url).asArray();
			List<T> entities = converterService.convert(array, forClass, ArrayList::new);
			
			entities.stream().forEach(x -> System.out.println(x.getId()));
			
			repository.save(entities.get(0));
			repository.save(entities.get(1));
			repository.save(entities.get(2));
			repository.save(entities.get(3));
			repository.save(entities.get(4));
			repository.save(entities.get(5));
			
			//repository.save(entities);
		}
	}
	
	public void importMaterials() {
		
		try {
			
			ConverterService service = new ConverterService();
			
			String licensesUrl = restCollection.get(License.class);
			Map<Long, JsonNode> licenses = find(licensesUrl).asMap();
			service.setLicenses(licenses);
			
			String lecturersUrl = restCollection.get(License.class);
			Map<Long, JsonNode> lecturers = find(lecturersUrl).asMap();
			service.setLecturers(lecturers);
			
			String materialUrl = restResource.get(Material.class);
			AjaxResult m = findOne(3325, materialUrl);
			JsonNode n = m.asNode();
			Material material = service.convert(n, Material.class);
		
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(material));
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

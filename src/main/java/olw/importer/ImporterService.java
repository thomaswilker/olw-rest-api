package olw.importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import olw.model.AbstractEntity;
import olw.model.Area;
import olw.model.Collection;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Material;
import olw.model.Section;
import olw.model.Semester;
import olw.repository.AreaRepository;
import olw.repository.CollectionRepository;
import olw.repository.LecturerRepository;
import olw.repository.LicenseRepository;
import olw.repository.MaterialRepository;
import olw.repository.SectionRepository;
import olw.repository.index.IndexedMaterialRepository;

@Service
public class ImporterService {

	@Autowired
	ConverterService converterService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	private final RestTemplate template = new RestTemplate();
	private final String api = "https://www.openlearnware.de/olw-rest-db/api/";
	private final Logger logger = Logger.getLogger(this.getClass());
	
	private final Map<Class<? extends AbstractEntity>, String> restResource = new HashMap<>();
	private final Map<Class<? extends AbstractEntity>, String> restCollection = new HashMap<>();
	
	@Autowired
	AreaRepository areaRepository;
	
	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	CollectionRepository collectionRepository;
	
	@Autowired
	LecturerRepository lecturerRepository;
	
	@Autowired
	LicenseRepository licenseRepository;
	
	@Autowired
	SectionRepository sectionRepository;
	
	@Autowired
	IndexedMaterialRepository indexedMaterialRepository;
	
	@Autowired
	IndexedMaterialRepository indexedCollectionRepository;
	
	
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
		restResource.put(Collection.class, "collection-detailview/index");
		
		// Add urls for multi rest resources
		restCollection.put(Material.class, "resource-overview/filter/index/all");
		restCollection.put(License.class, "license/all");
		restCollection.put(Lecturer.class, "user/all");
		restCollection.put(Language.class, "language/all");
		restCollection.put(Area.class, "area/all");
		restCollection.put(Semester.class, "semester/all");
		
	}
	
	private <T extends AbstractEntity> JpaRepository<T, Long> getRepository(Class<T> forClass) {
		return (JpaRepository<T, Long>) repositories.getRepositoryFor(forClass);
	}
	
	
	private <T extends AbstractEntity> Map<Long, Long> importEntity(Class<T> forClass) {
	
		String url = restCollection.get(forClass);
		Map<Long, Long> result = new HashMap<>();
		
		if(url != null) {
			JpaRepository<T, Long> repository = getRepository(forClass); 
			repository.deleteAll();
			try {
				ArrayNode array = find(url, null).asArray();
				List<T> list = converterService.convert(array, forClass, ArrayList::new);
				
				result = list.stream().collect(Collectors.toMap(e -> e.getId(), e -> { repository.save(e); return e.getId();}));
			} catch(Exception e) {
				System.out.println("error: " + e.getMessage());
			}
		}
		
		return result;
	}
	
	private Map<Class<? extends AbstractEntity>, Map<Long,Long>> importEntities(List<Class<? extends AbstractEntity>> forClasses) {
		
		return forClasses.stream().collect(Collectors.toMap(c -> c, c -> importEntity(c)));
	}
	
	private void createSections(Map<Long,Long> areaMap) {
		
		System.out.println("create sections");
		
		Section iw = sectionRepository.save(new Section("iw", "Ingenieurswissenschaften"));
		Section nw = sectionRepository.save(new Section("nw", "Naturwissenschaften"));
		Section gw = sectionRepository.save(new Section("gw", "Geisteswissenschaften"));
		
		List<Long> iwAreas = mapEntities(areaMap, 1,2,4,9,11);
		List<Long> nwAreas = mapEntities(areaMap, 5,6);
		List<Long> gwAreas = mapEntities(areaMap, 3,7,8,10,12,13);
		
		areaRepository.findAll(iwAreas).stream().forEach(a -> {a.setSection(iw); areaRepository.save(a);});
		areaRepository.findAll(nwAreas).stream().forEach(a -> {a.setSection(nw); areaRepository.save(a);});
		areaRepository.findAll(gwAreas).stream().forEach(a -> {a.setSection(gw); areaRepository.save(a);});
		
		sectionRepository.save(Arrays.asList(iw,nw,gw));
	}
	
	private List<Long> mapEntities(Map<Long,Long> entityMap, Integer... ids) {
		return Arrays.stream(ids).map(i -> entityMap.get(i.longValue())).collect(Collectors.toList());
	}
	
	
	public Map<Long, Material> importMaterials() {
		
		Map<Long, Material> materials = new HashMap<>();
		
		try {
			
			indexedMaterialRepository.deleteAll();
			materialRepository.deleteAll();
			lecturerRepository.deleteAll();
			licenseRepository.deleteAll();
			
			List<Class<? extends AbstractEntity>> entities = Arrays.asList(Semester.class, Area.class, License.class, Language.class, Lecturer.class);
			Map<Class<? extends AbstractEntity>, Map<Long, Long>> entityMaps = importEntities(entities);
			
			createSections(entityMaps.get(Area.class));
			
			String materialUrl = restCollection.get(Material.class);
			List<JsonNode> list = StreamSupport.stream(find(materialUrl, null).asArray().spliterator(), false)
															  	.collect(Collectors.toList());
			
			
			converterService.setEntityMaps(entityMaps);
			List<Material> materialList = converterService.convert(list, Material.class, ArrayList::new);
			materials = materialList.stream().collect(Collectors.toMap(m-> m.getId(), m -> (Material) materialRepository.save(m)));
			
			
		} catch(Exception e) {
			logger.info(e.getMessage());
		}
		
		return materials;
	}
	
	public void importCollection(Long id) {
		
		try {
			indexedCollectionRepository.deleteAll();
			collectionRepository.deleteAll();
			
			converterService.setMaterialMap(importMaterials());
			
			String allIdsUrl = "/collection-overview/filter/index/all";
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("pick", "id");
			
			
			StreamSupport.stream(find(allIdsUrl,params).asArray().spliterator(), false).forEach(i -> {
				try {
					String url = restResource.get(Collection.class);
					JsonNode collectionNode = findOne(i.get("id").asInt(), url).asNode();
					if(!collectionNode.get("name").isNull() && collectionNode.get("name").asText().length() > 0) {
						Collection c = converterService.convert(collectionNode, Collection.class);
						collectionRepository.save(c);
					}
				} catch (Exception e) {
					logger.info("error with collection " + i.get("id").asText());
					logger.info("message: " + e.getLocalizedMessage());
				}
			});
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	private AjaxResult findOne(Integer id, String path) {
		return find(path + "/" + id, null);
	}
	
	private AjaxResult find(String path, MultiValueMap<String, String> params) {
		return new AjaxResult(template.getForObject(select(path, params).toUriString(), String.class));
	}
	
	private UriComponentsBuilder select(String path, MultiValueMap<String, String> params) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(api).path(path); 
		if(params != null) {
			builder.queryParams(params);
		}
		return builder;
	}
	
}

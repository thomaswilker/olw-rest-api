package olw.importer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Getter;
import lombok.Setter;
import olw.model.AbstractEntity;
import olw.model.Area;
import olw.model.Collection;
import olw.model.Language;
import olw.model.Lecturer;
import olw.model.License;
import olw.model.Material;
import olw.model.Semester;
import olw.model.Semester.Part;

@Getter
@Setter
@Service
public class ConverterService {

	private final ObjectMapper mapper = new ObjectMapper();
	private Map<Class<? extends AbstractEntity>, Map<Long,Long>> entityMaps = new HashMap<>();
	private Map<Long, Material> materialMap = new HashMap<>();
	
	final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ApplicationContext context;
	
	private Repositories repositories;
	
	@PostConstruct
	public void postConstruct() {
		 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// Retrieve all Spring data repositories
		repositories = new Repositories(context);
		System.out.println("postConstruct");
	}
	
	private <T extends AbstractEntity> JpaRepository<T, Long> getRepository(Class<T> forClass) {
		return (JpaRepository<T, Long>) repositories.getRepositoryFor(forClass);
	}
	
	private <T extends AbstractEntity> T findOne(Class<T> ofClass, JsonNode node) {
		
		return findOneById(ofClass, node.get("id").asLong());
	}
	
	private <T extends AbstractEntity> T findOneById(Class<T> ofClass, Long id) {
		
		Long entityId = entityMaps.get(ofClass).get(id);
		return getRepository(ofClass).findOne(entityId);
	}
	
	public <T extends AbstractEntity, C extends java.util.Collection<T>> C findAll(Class<T> asClass, Iterable<JsonNode> array, Supplier<C> supplier) {
		
		return StreamSupport.stream(array.spliterator(), false).map(a -> findOneById(asClass, a.get("id").asLong())).collect(Collectors.toCollection(supplier));
	}
	
	private Collection convert(JsonNode node, Collection o)  {
		
		o.setOldId(o.getId());

		o.setDate(new Date(node.get("creationDate").asLong()));
		Set<Area> areas = findAll(Area.class, node.get("areas"), HashSet::new);
		o.setAreas(areas);
		Set<Lecturer> lecturers = findAll(Lecturer.class, node.get("users"), HashSet::new);
		o.setLecturers(lecturers);
		
		Set<Semester> semesters = findAll(Semester.class, node.get("semesters"), HashSet::new);
		o.setSemesters(semesters);
		
		JsonNode resources = node.get("resources");
		JsonNode rubrics = node.get("rubrics");
		
		List<Material> materials = StreamSupport.stream(node.get("collectionElements").spliterator(), false).flatMap(x -> {
			
			String p = x.asText();
			if(resources.has(p)) {
				Material m = materialMap.get(x.asLong());
				return Stream.of(m);
			} else if(rubrics.has(p)) {
				return StreamSupport.stream(rubrics.get(p).get("resources").spliterator(), false)
						.map(r -> materialMap.get(r.asLong()));
			} else {
				return Stream.empty();
			}
			
		}).collect(Collectors.toList());
		
		o.setMaterials(materials);
		
		return o;
	}
	
	private Material convert(JsonNode node, Material o)  {
		
		o.setOldId(o.getId());
		License license = findOneById(License.class, node.get("licenseType").asLong());
		o.setLicense(license);
		
		Set<Lecturer> lecturer = findAll(Lecturer.class, node.get("users"), HashSet::new);
		o.setLecturers(lecturer);
		
		ArrayNode langArray = (ArrayNode) node.get("languages");
		Set<Language> lang = StreamSupport.stream(langArray.spliterator(), false)
				.map(u -> findOne(Language.class, u))
				.collect(Collectors.toSet());
		o.setLanguages(lang);
		
		return o;
	}
	
	private License convert(JsonNode node, License o) {
		o.setName(node.get("code").asText());
		return o;
	}
	
	private Lecturer convert(JsonNode node, Lecturer o) {
		return o;
	}
	
	private Language convert(JsonNode node, Language o) {
		return o;
	}
	
	private Area convert(JsonNode node, Area o) {
		return o;
	}
	
	private Semester convert(JsonNode node, Semester o) {
		
		if(node.get("part").asInt() == 1) {
			o.setPart(Part.WiSe);
		} else {
			o.setPart(Part.SoSe);
		}
		
		return o;
	}
	
	public <T, C extends java.util.Collection<T>> C convert(Iterable<JsonNode> array, Class<T> asClass, Supplier<C> supplier) {
		System.out.println("convert iterable of class " + asClass);
		
		return StreamSupport.stream(array.spliterator(), false)
				 .map(n -> convert(n, asClass))
				 .collect(Collectors.toCollection(supplier));
	}
	
	private <T> Method getConverterMethod(Class<T> asClass) throws NoSuchMethodException, SecurityException {
		return this.getClass().getDeclaredMethod("convert", JsonNode.class, asClass);
	}
	
	public <T> T convert(JsonNode o, Class<T> asClass) {
		
		T entity = mapper.convertValue(o, asClass);
		try {
			Method m = getConverterMethod(asClass);
			Object object = m.invoke(this, o, entity);
			entity = (T) object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return entity;
	}
}
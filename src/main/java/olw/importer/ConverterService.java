package olw.importer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Getter;
import lombok.Setter;
import olw.model.Area;
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
	private Map<Long, Long> licenses;
	private Map<Long, Long> lecturers;
	private Map<Long, Long> languages;
	
	@PersistenceContext
	private EntityManager em;
	
	@PostConstruct
	public void postConstruct() {
		 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
	}
	
	@Transactional
	private Material convert(JsonNode node, Material o)  {
		
		Long licenseId = node.get("licenseType").asLong();
		License license = em.find(License.class, licenses.get(licenseId));
		
		o.setLicense(license);
		
		ArrayNode users = (ArrayNode) node.get("users");
		Set<Lecturer> l = StreamSupport.stream(users.spliterator(), false)
								.map(u -> em.find(Lecturer.class, lecturers.get(u.get("id").asLong())))
								.collect(Collectors.toSet());
		o.setLecturers(l);
		
		ArrayNode langArray = (ArrayNode) node.get("languages");
		Set<Language> lang = StreamSupport.stream(langArray.spliterator(), false)
				.map(u -> em.find(Language.class, languages.get(u.get("id").asLong())))
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
	
	public <T, C extends Collection<T>> C convert(Iterable<JsonNode> array, Class<T> asClass, Supplier<C> supplier) {
		
		return StreamSupport.stream(array.spliterator(), false)
				 .map(n -> convert(n, asClass))
				 .collect(Collectors.toCollection(supplier));
	}
	
	
	private <T> Method getConverterMethod(Class<T> asClass) throws NoSuchMethodException, SecurityException {
		return ConverterService.class.getDeclaredMethod("convert", JsonNode.class, asClass);
	}
	
	public <T> T convert(JsonNode o, Class<T> asClass) {
		
		T entity = mapper.convertValue(o, asClass);
		try {
			Method m = getConverterMethod(asClass);
			entity = (T) m.invoke(this, o, entity);
		} catch (Exception e) {
			
		}
		
		return entity;
	}
}
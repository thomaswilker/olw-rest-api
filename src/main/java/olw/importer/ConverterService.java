package olw.importer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
	private Map<Long, Long> licenses = new HashMap<>();
	private Map<Long, Long> lecturers = new HashMap<>();
	private Map<Long, Long> languages = new HashMap<>();
	
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
	
	private <T extends AbstractEntity> T findOne(Class<T> ofClass, Long id) {
		return getRepository(ofClass).findOne(id);
	}
	
	private Material convert(JsonNode node, Material o)  {
		
		
		JsonNode licenseNode = node.get("licenseType");
		Long licenseId = 1l; 
		if(licenseNode != null) {
			licenseId = licenseNode.asLong();
		} 
		
		License license = findOne(License.class, licenses.get(licenseId));
		o.setLicense(license);
		
		ArrayNode users = (ArrayNode) node.get("users");
		
		Set<Lecturer> l = StreamSupport.stream(users.spliterator(), false)
								.map(u -> findOne(Lecturer.class, lecturers.get(u.get("id").asLong())))
								.collect(Collectors.toSet());
		o.setLecturers(l);
		
		ArrayNode langArray = (ArrayNode) node.get("languages");
		Set<Language> lang = StreamSupport.stream(langArray.spliterator(), false)
				.map(u -> findOne(Language.class, languages.get(u.get("id").asLong())))
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
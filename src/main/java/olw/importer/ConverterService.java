package olw.importer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

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
	private Map<Long, JsonNode> licenses;
	private Map<Long, JsonNode> lecturers;
	
	@PostConstruct
	public void postConstruct() {
		 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private Material convert(JsonNode node, Material o)  {
		
		Long licenseId = node.get("licenseType").asLong();
		License license = convert(licenses.get(licenseId), License.class);
		
		o.setLicense(license);
		
		ArrayNode users = (ArrayNode) node.get("users");
		Set<Lecturer> lecturers = convert(users, Lecturer.class, HashSet::new);
		o.setLecturers(lecturers);
		
		
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
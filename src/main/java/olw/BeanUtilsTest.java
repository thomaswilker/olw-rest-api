package olw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import olw.model.AbstractEntity;
import olw.model.License;
import olw.model.Material;

public class BeanUtilsTest {

	static ObjectMapper mapper = new ObjectMapper();
	static RestTemplate template = new RestTemplate();
	static String api = "https://www.openlearnware.de/olw-rest-db/api/";
	static String Resource = "resource-detailview/index";
	static String Licenses = "license/all";
	
	
	static class ConverterService {

		HashMap<Class<?>, Function<JsonNode,?>> converter = new HashMap<>();
		final static ObjectMapper mapper = new ObjectMapper();
		
		public ConverterService() {
		
			converter.put(License.class, (JsonNode n) -> {
				return mapper.convertValue(n, License.class);
			});
			
			converter.put(AbstractEntity.class, (JsonNode n) -> {
				return mapper.convertValue(n, License.class);
			});
		}
		
		public <T> T convert(Class<T> asClass) {
			return converter.get(asClass);
		}
	}
	
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, JsonProcessingException, IOException {
		
		
		
		Material m = findOne(1, Resource).as(Material.class);
		asStream(find(Licenses).asArray()).collect(Collectors.toMap(x -> x, y -> y.));
		
	}

	static <T> Stream<T> asStream(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false /* or whatever */);
	}
	
	static String[] pathSegments(String p) {
		return p.split("/");
	}
	
	static Result findOne(Integer id, String... pathSegments) {
		pathSegments = (String[]) ArrayUtils.add(pathSegments, id.toString());
		return find(pathSegments);
	}
	
	static Result find(String... pathSegments) {
		return new Result(template.getForObject(select(pathSegments).toUriString(), String.class));
	}
	
	static UriComponentsBuilder select(String... path) {
		List<String> segments = Arrays.asList(path).stream().flatMap(s -> Stream.of(s.split("/"))).collect(Collectors.toList());
		return UriComponentsBuilder.fromHttpUrl(api).pathSegment(segments.toArray(new String[]{}));
	}
	
	static class Result {
		
		String json;
		public Result(String json) {
			this.json = json;
		}
		
		public ArrayNode asArray() throws JsonProcessingException, IOException {
			return mapper.readValue(json, ArrayNode.class);
		}
		
		public ObjectNode asObject() throws JsonProcessingException, IOException {
			return mapper.readValue(json, ObjectNode.class);
		}
		
		public <T> T as(Class<T> asClass) throws JsonProcessingException, IOException {
			return mapper.readValue(json, asClass);
		}
		
		@Override 
		public String toString() {
			return json;
		}
	}
	
	
	
	
	
	
}

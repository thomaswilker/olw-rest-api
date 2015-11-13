package olw.importer;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


class AjaxResult {
	
	String json;
	ObjectMapper mapper = new ObjectMapper();
	
	public AjaxResult(String json) {
		this.json = json;
	}
	
	public Map<Long, JsonNode> asMap() throws JsonProcessingException, IOException {
		
		Stream<JsonNode> nodeStream = StreamSupport.stream(asArray().spliterator(), false);
		Map<Long, JsonNode> nodes = nodeStream.collect(Collectors.toMap(a -> a.get("id").asLong(), x -> x));
		return nodes;
		
	}
	
	public ArrayNode asArray() throws JsonProcessingException, IOException {
		return mapper.readValue(json, ArrayNode.class);
	}
	
	public ObjectNode asObject() throws JsonProcessingException, IOException {
		return mapper.readValue(json, ObjectNode.class);
	}
	
	public JsonNode asNode() throws JsonProcessingException, IOException {
		return mapper.readTree(json);
	}
	
	public <T> T as(Class<T> asClass) throws JsonProcessingException, IOException {
		return mapper.readValue(json, asClass);
	}
	
	@Override 
	public String toString() {
		return json;
	}
}
package olw.service;

import java.util.stream.Collectors;

import olw.model.Collection;
import olw.model.index.IndexedCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CollectionToIndexConverter implements Converter<Collection, IndexedCollection>{

	@Autowired
	MaterialToIndexConverter materialConverter;
	
	@Override
	public IndexedCollection convert(Collection collection) {
		ObjectMapper mapper = new ObjectMapper();
		IndexedCollection ic = mapper.convertValue(collection, IndexedCollection.class);
		
		ic.setAreas(collection.getAreas());
		ic.setLecturers(collection.getLecturers());
		ic.setMaterials(collection.getMaterials().stream().map(m -> materialConverter.convert(m)).collect(Collectors.toList()));
		ic.setTags(collection.getTags());
		
		return ic;
	}

	
	
}

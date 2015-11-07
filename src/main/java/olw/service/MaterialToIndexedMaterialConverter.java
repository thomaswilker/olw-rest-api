package olw.service;

import olw.model.Material;
import olw.model.index.IndexedMaterial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MaterialToIndexedMaterialConverter implements Converter<Material, IndexedMaterial>{

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;
	
	@Override
	public IndexedMaterial convert(Material material) {
		return mapper.convertValue(material, IndexedMaterial.class);
	}

	
	
}

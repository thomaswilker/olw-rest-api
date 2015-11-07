package olw.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import olw.model.Material;
import olw.model.index.IndexedMaterial;

@Component
public class MaterialToIndexConverter implements Converter<Material, IndexedMaterial>{

	@Override
	public IndexedMaterial convert(Material material) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(material, IndexedMaterial.class);
	}

	
	
}

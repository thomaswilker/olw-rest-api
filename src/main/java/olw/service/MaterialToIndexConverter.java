package olw.service;

import java.util.ArrayList;

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
		IndexedMaterial im = mapper.convertValue(material, IndexedMaterial.class);
		
		im.setLanguages(new ArrayList<>(material.getLanguages()));
		im.setLecturer(new ArrayList<>(material.getLecturer()));
		im.setTags(new ArrayList<>(material.getTags()));
		return im;
	}

	
	
}

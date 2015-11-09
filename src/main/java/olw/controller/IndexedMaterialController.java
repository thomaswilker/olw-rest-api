package olw.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import olw.model.index.IndexedMaterial;
import olw.repository.index.IndexedMaterialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;




@RepositoryRestController
@RequestMapping("/index/materials")
public class IndexedMaterialController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	IndexedMaterialRepository repository;
	
	@Autowired
	private EntityLinks entityLinks;

	@Autowired
	@Qualifier("objectMapper")
	ObjectMapper mapper;
	
	@RequestMapping("/{id}")
	public ResponseEntity<Resource<IndexedMaterial>> getMaterial(@PathVariable("id") Long id) {
		
		IndexedMaterial m = repository.findOne(id);
		Resource<IndexedMaterial> r = new Resource<IndexedMaterial>(m);
		return new ResponseEntity<Resource<IndexedMaterial>>(r, HttpStatus.OK);
	}
	
	
	private Object mapProperties(Object o, Set<String> fields, Boolean pick) {
		
		ObjectNode node = mapper.valueToTree(o);
		return pick ? node.retain(fields) : node.without(fields);
	}
	
	@RequestMapping("greeting")
	public String greet() {
		return "greets";
	}
	
	@RequestMapping
	public ResponseEntity<PagedResources<Object>> getMaterials(Pageable p, 
			      											   @RequestParam MultiValueMap<String, Set<String>> params,
			      											   @RequestParam(required=false, value="fields") Set<String> fields,
															   @RequestParam(required=false, defaultValue="true", value="pick") Boolean pick)  {
		
		Page<IndexedMaterial> page = repository.findAll(p);
		PageMetadata metadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		
		List<Object> content = new ArrayList<Object>();
		content.addAll(page.getContent());
		
		
		if(fields != null && fields.size() > 0) {
			content = page.getContent().stream().map(m -> mapProperties(m, fields, pick)).collect(Collectors.toList());
		}
				
		PagedResources<Object> resource = new PagedResources<Object>(content, metadata);
		resource.add(linkTo(methodOn(IndexedMaterialController.class).getAllMaterials()).withRel("all"));
		resource.add(linkTo(IndexedMaterialController.class).withSelfRel());
		
	    return new ResponseEntity<PagedResources<Object>>(resource, HttpStatus.OK);
	}

	@RequestMapping("/all")
	public ResponseEntity<Resources<IndexedMaterial>> getAllMaterials() {
		
		Resources<IndexedMaterial> resources = new Resources<IndexedMaterial>(repository.findAll());
	    return new ResponseEntity<Resources<IndexedMaterial>>(resources, HttpStatus.OK);
	}
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(linkTo(IndexedMaterial.class).withSelfRel());
		return resource;
	}
	
	
}

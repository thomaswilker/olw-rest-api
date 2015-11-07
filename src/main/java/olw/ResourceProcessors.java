package olw;

import olw.controller.IndexedMaterialController;
import olw.model.Material;
import olw.model.index.IndexedMaterial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Configuration
public class ResourceProcessors {

	@Autowired
	EntityLinks entityLinks;
	
//	@Bean
//	public ResourceProcessor<Resource<Material>> materialProcessor () {
//
//	   return new ResourceProcessor<Resource<Material>>() {
//
//	     @Override
//	     public Resource<Material> process(Resource<Material> resource) {
//	    	System.out.println("resource of material");
//	    	resource.add(linkTo(methodOn(IndexedMaterialController.class).getMaterial(resource.getContent().getId())).withSelfRel());
//	    	return resource;
//	     }
//		
//	   };
//	}
//	
//	@Bean
//	public ResourceProcessor<Resources<IndexedMaterial>> indexedMaterialProcessor() {
//
//	   return new ResourceProcessor<Resources<IndexedMaterial>>() {
//
//	     @Override
//	     public Resources<IndexedMaterial> process(Resources<IndexedMaterial> resource) {
//	    	System.out.println("resource of indexed material");
//		    return resource;
//	     }
//	   };
//	}
	
}

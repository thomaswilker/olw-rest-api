package olw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;


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

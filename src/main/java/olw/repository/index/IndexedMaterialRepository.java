package olw.repository.index;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import olw.model.index.IndexedMaterial;

@RepositoryRestResource(exported=false)
public interface IndexedMaterialRepository extends ElasticsearchCrudRepository<IndexedMaterial, Long> {

	public Page<IndexedMaterial> findAllByName(Pageable page, String name);
	
	
}

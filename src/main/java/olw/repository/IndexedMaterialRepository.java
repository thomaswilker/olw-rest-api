package olw.repository;

import olw.model.index.IndexedMaterial;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface IndexedMaterialRepository extends ElasticsearchCrudRepository<IndexedMaterial, Long>{

}

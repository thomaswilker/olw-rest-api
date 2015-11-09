package olw.repository;

import olw.model.index.IndexedCollection;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface IndexedCollectionRepository extends ElasticsearchCrudRepository<IndexedCollection, Long>{

}

package olw.repository;

import org.hibernate.mapping.IndexedCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported=false)
public interface IndexedCollectionRepository extends JpaRepository<IndexedCollection, Long>{

}

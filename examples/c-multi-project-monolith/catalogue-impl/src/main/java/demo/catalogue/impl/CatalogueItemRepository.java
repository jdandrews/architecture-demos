package demo.catalogue.impl;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CatalogueItemRepository extends ListCrudRepository<CatalogueItemEntity, Long> {

    List<CatalogueItemEntity> findAllByIdIn(Iterable<Long> ids);
}

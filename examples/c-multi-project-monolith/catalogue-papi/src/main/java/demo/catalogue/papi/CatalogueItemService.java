package demo.catalogue.papi;

import java.util.List;
import java.util.Optional;

public interface CatalogueItemService {

    Optional<CatalogueItem> findWithId(long id);

    List<CatalogueItem> findAllWithId(Iterable<Long> ids);

    CatalogueItem add(NewCatalogueItem newCatalogueItem);
}

package demo.catalogue;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CatalogueItemService {

    private final CatalogueItemRepository repository;

    public CatalogueItemService(final CatalogueItemRepository repository) {
        this.repository = repository;
    }

    public Optional<CatalogueItemEntity> findWithId(final long id) {
        return repository.findById(id);
    }

    public List<CatalogueItemEntity> findAllWithId(final Set<Long> id) {
        return repository.findAllById(id);
    }

    public CatalogueItemEntity add(final CatalogueItemEntity entity) {
        return repository.save(entity);
    }
}

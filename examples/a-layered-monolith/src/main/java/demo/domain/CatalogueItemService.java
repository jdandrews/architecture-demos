package demo.domain;

import demo.dao.CatalogueItemEntity;
import demo.dao.CatalogueItemRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatalogueItemService {

    private final CatalogueItemRepository repository;

    public CatalogueItemService(final CatalogueItemRepository repository) {
        this.repository = repository;
    }

    public Optional<CatalogueItemEntity> findWithId(final long id) {
        return repository.findById(id);
    }

    public CatalogueItemEntity add(final CatalogueItemEntity entity) {
        return repository.save(entity);
    }
}

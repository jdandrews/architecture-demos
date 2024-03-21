package demo.catalogue;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CatalogueItemService {

    private final CatalogueItemRepository repository;
    private final CatalogueItemGateway gateway;

    public CatalogueItemService(final CatalogueItemRepository repository, final CatalogueItemGateway gateway) {
        this.repository = repository;
        this.gateway = gateway;
    }

    public Optional<CatalogueItemEntity> findWithId(final long id) {
        return repository.findById(id);
    }

    public CatalogueItemEntity add(final CatalogueItemEntity entity) {
        return repository.save(entity)
                .with(gateway::newCatalogueItem);
    }
}

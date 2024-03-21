package demo.catalogue.impl;

import demo.catalogue.papi.CatalogueItem;
import demo.catalogue.papi.CatalogueItemService;
import demo.catalogue.papi.NewCatalogueItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class CatalogueItemServiceImpl implements CatalogueItemService {

    private final CatalogueItemRepository repository;

    public CatalogueItemServiceImpl(final CatalogueItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<CatalogueItem> findWithId(final long id) {
        return repository.findById(id)
                .map(CatalogueItemEntity::toDomain);
    }

    @Override
    public List<CatalogueItem> findAllWithId(final Iterable<Long> ids) {
        requireNonNull(ids);

        return repository.findAllByIdIn(ids).stream()
                .map(CatalogueItemEntity::toDomain)
                .toList();
    }

    @Override
    public CatalogueItem add(final NewCatalogueItem newCatalogueItem) {
        requireNonNull(newCatalogueItem);

        return newCatalogueItem
                .map(CatalogueItemEntity::of)
                .map(repository::save)
                .map(CatalogueItemEntity::toDomain);
    }
}

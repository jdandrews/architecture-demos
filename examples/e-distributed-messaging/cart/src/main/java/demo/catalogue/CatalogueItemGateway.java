package demo.catalogue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class CatalogueItemGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogueItemGateway.class);

    private final ObjectMapper objectMapper;
    private final CatalogueItemRepository repository;

    public CatalogueItemGateway(final ObjectMapper objectMapper, final CatalogueItemRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    public void newCatalogueItem(final String json) {
        requireNonNull(json);

        CatalogueItemGateway.LOGGER.info("Received new catalogue item {}", json);
        final CatalogueItemEntity entity = fromJson(json).toEntity();
        repository.save(entity);
    }

    private CatalogueItemTo fromJson(final String json) {
        requireNonNull(json);

        try {
            return objectMapper.readValue(json, CatalogueItemTo.class);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

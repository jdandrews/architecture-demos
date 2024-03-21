package demo.catalogue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CatalogueItemGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogueItemGateway.class);

    private final RestTemplate restTemplate;

    public CatalogueItemGateway(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<CatalogueItemEntity> findWithId(final long id) {
        try {
            final ResponseEntity<CatalogueItemTo> entity = restTemplate.getForEntity("/catalogue/item/%d".formatted(id), CatalogueItemTo.class);
            if (HttpStatus.OK.equals(entity.getStatusCode())) {
                return Optional.ofNullable(entity.getBody())
                        .map(CatalogueItemTo::toEntity);
            }
        } catch (final HttpClientErrorException.NotFound e) {
            LOGGER.error("Catalogue Item with id {} not found", id, e);
        }
        return Optional.empty();
    }

    public List<CatalogueItemEntity> findAllWithId(final Set<Long> ids) {
        final String stringIds = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        final ParameterizedTypeReference<List<CatalogueItemTo>> type = new ParameterizedTypeReference<>() {};
        return restTemplate.exchange("/catalogue/item/all/%s".formatted(stringIds), HttpMethod.GET, null, type)
                .getBody() /* TODO: need to check for nulls */
                .stream().map(CatalogueItemTo::toEntity)
                .toList();
    }
}

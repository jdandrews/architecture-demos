package demo.catalogue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class CatalogueItemGateway {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public CatalogueItemGateway(final RabbitTemplate rabbitTemplate, final ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void newCatalogueItem(final CatalogueItemEntity item) {
        requireNonNull(item);

        final String json = toJson(item);
        rabbitTemplate.convertAndSend("demo-exchange", "demo.catalogue.new", json);
    }

    private String toJson(final CatalogueItemEntity item) {
        requireNonNull(item);

        try {
            return objectMapper.writeValueAsString(CatalogueItemTo.of(item));
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

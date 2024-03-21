package demo.catalogue.impl;

import demo.catalogue.papi.CatalogueItem;
import demo.catalogue.papi.CatalogueItemService;
import demo.catalogue.papi.NewCatalogueItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/catalogue/item")
public class CatalogueItemController {

    private final demo.catalogue.papi.CatalogueItemService service;

    public CatalogueItemController(final CatalogueItemService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogueItem> get(@PathVariable(value = "id") final long id) {
        return service.findWithId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<CatalogueItem> add(@RequestBody final NewCatalogueItem item) {
        return service.add(item)
                .map(e -> ResponseEntity.created(location(e)).body(e));
    }

    private static URI location(final CatalogueItem item) {
        requireNonNull(item);
        return URI.create("/catalogue/item/%d".formatted(item.id()));
    }
}

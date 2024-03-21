package demo.catalogue.papi;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public record CatalogueItem(long id, String caption, String description) {

    public <T> T map(final Function<CatalogueItem, T> mapper) {
        requireNonNull(mapper);
        return mapper.apply(this);
    }
}

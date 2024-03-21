package demo.catalogue.papi;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public record NewCatalogueItem(String caption, String description) {

    public <T> T map(final Function<NewCatalogueItem, T> mapper) {
        requireNonNull(mapper);
        return mapper.apply(this);
    }
}

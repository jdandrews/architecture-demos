package demo.common;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class Mapper {

    public static <T, R> R mapIfNotNull(final T object, final Function<T, R> mapper) {
        requireNonNull(mapper);

        return Optional.ofNullable(object)
                .map(mapper)
                .orElse(null);
    }

    private Mapper() {}
}

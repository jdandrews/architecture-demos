package demo.rest;

import demo.dao.CartEntity;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record CartTo(Long id, List<CartItemTo> items) {

    public CartTo {
        items = List.copyOf(items);
    }

    public static CartTo of(final CartEntity entity) {
        requireNonNull(entity);

        final Long id = entity.id();
        final List<CartItemTo> items = entity.items().stream()
                .map(CartItemTo::of)
                .toList();

        return new CartTo(id, items);
    }
}

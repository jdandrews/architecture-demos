package demo.rest;

import demo.dao.CartItemEntity;

import static java.util.Objects.requireNonNull;

public record CartItemTo(Long id, String caption, int quantity) {

    public static CartItemTo of(final CartItemEntity entity) {
        requireNonNull(entity);

        final Long id = entity.itemId();
        final String caption = entity.itemCaption();
        final int quantity = entity.quantity();

        return new CartItemTo(id, caption, quantity);
    }
}

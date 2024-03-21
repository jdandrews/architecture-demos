package demo.cart.papi;

import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public record CartItem(long id, String caption, int quantity) {

    public <T> T map(final Function<CartItem, T> mapper) {
        requireNonNull(mapper);
        return mapper.apply(this);
    }

    public CartItem withCaption(final String caption) {
        return Objects.equals(this.caption, caption)
                ? this
                : new CartItem(this.id, caption, this.quantity);
    }
}

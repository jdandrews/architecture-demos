package demo.cart.papi;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public record Cart(long id, List<CartItem> items) {

    public Cart {
        items = List.copyOf(items);
    }

    public <T> T map(final Function<Cart, T> mapper) {
        requireNonNull(mapper);
        return mapper.apply(this);
    }

    public Cart withItems(List<CartItem> items) {
        return Objects.equals(this.items, items)
                ? this
                : new Cart(id, items);
    }
}

package demo.cart.impl;

import demo.cart.papi.CartItem;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

import static demo.common.Mapper.mapIfNotNull;
import static java.util.Objects.requireNonNull;

@Entity(name = "cart_item")
public class CartItemEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CartItemEntityPrimaryKey id;
    private int quantity;

    public CartItemEntity() {}

    public CartItemEntity(final CartItemEntityPrimaryKey id, final int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public CartItemEntityPrimaryKey id() {
        return id;
    }

    public Long itemId() {
        return mapIfNotNull(id, CartItemEntityPrimaryKey::itemId);
    }

    public int quantity() {
        return quantity;
    }

    public void adjustQuantityBy(final int offset) {
        this.quantity += offset;
    }

    public CartItem toDomain() {
        return map(entity -> new CartItem(entity.itemId(), null, entity.quantity()));
    }

    public <T> T map(final Function<CartItemEntity, T> mapper) {
        requireNonNull(mapper);
        return mapper.apply(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final CartItemEntity other = (CartItemEntity) object;
        return Objects.equals(id, other.id)
                && quantity == other.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }

    @Override
    public String toString() {
        return "CartItemEntity[id=%s, quantity=%d]"
                .formatted(id, quantity);
    }
}

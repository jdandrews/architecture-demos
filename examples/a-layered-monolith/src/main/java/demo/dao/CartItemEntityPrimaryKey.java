package demo.dao;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static demo.common.Mapper.mapIfNotNull;

@Embeddable
public class CartItemEntityPrimaryKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cartId")
    private CartEntity cart;
    @ManyToOne(optional = false)
    @JoinColumn(name = "itemId")
    private CatalogueItemEntity item;

    public CartItemEntityPrimaryKey() {}

    public CartItemEntityPrimaryKey(final CartEntity cart, final CatalogueItemEntity item) {
        this.cart = cart;
        this.item = item;
    }

    public Long cartId() {
        return mapIfNotNull(cart, CartEntity::id);
    }

    public Long itemId() {
        return mapIfNotNull(item, CatalogueItemEntity::id);
    }

    public String itemCaption() {
        return mapIfNotNull(item, CatalogueItemEntity::caption);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final CartItemEntityPrimaryKey other = (CartItemEntityPrimaryKey) object;
        return Objects.equals(cartId(), other.cartId())
                && Objects.equals(itemId(), other.itemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId(), itemId());
    }

    @Override
    public String toString() {
        return "CartItemEntityPrimaryKey[cartId=%d, itemId=%d]"
                .formatted(cartId(), itemId());
    }
}

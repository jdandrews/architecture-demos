package demo.cart.impl;

import demo.cart.papi.Cart;
import demo.cart.papi.CartItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Entity(name = "cart")
public class CartEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "id.cart", cascade = CascadeType.ALL)
    private List<CartItemEntity> items;

    public CartEntity() {}

    public Long id() {
        return id;
    }

    public List<CartItemEntity> items() {
        return items;
    }

    public void addItem(final Long itemId, final int quantity) {
        requireNonNull(itemId);

        final CartItemEntityPrimaryKey cartItemId = new CartItemEntityPrimaryKey(this, itemId);
        items.add(new CartItemEntity(cartItemId, quantity));
    }

    public CartItemEntity findCartItemWithId(final Long id) {
        return items.stream()
                .filter(i -> id.equals(i.itemId()))
                .findFirst()
                .orElse(null);
    }

    public Cart toDomain() {
        return map(entity -> new Cart(entity.id(), entity.toCartItemDomain()));
    }

    private List<CartItem> toCartItemDomain() {
        return items().stream()
                .map(CartItemEntity::toDomain)
                .toList();
    }

    public <T> T map(final Function<CartEntity, T> mapper) {
        requireNonNull(mapper);
        return mapper.apply(this);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final CartEntity other = (CartEntity) object;
        return Objects.equals(id, other.id)
                && Objects.equals(items, other.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items);
    }

    @Override
    public String toString() {
        return "CartEntity[id=%d, items=%s]".formatted(id, items);
    }
}

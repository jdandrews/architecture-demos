package demo.cart.papi;

import java.util.Optional;

public interface CartService {

    Optional<Cart> findWithId(long id);

    Optional<Cart> addItemToCart(long cartId, long itemId);
}

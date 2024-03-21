package demo.cart;

import demo.catalogue.CatalogueItemEntity;
import demo.catalogue.CatalogueItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CatalogueItemRepository itemRepository;

    public CartService(final CartRepository cartRepository,
                       final CatalogueItemRepository itemRepository) {
        this.cartRepository = requireNonNull(cartRepository);
        this.itemRepository = requireNonNull(itemRepository);
    }

    public Optional<CartEntity> findWithId(final long id) {
        return cartRepository.findById(id);
    }

    @Transactional
    public Optional<CartEntity> addItemToCart(final long cartId, final long itemId) {
        final Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
        final Optional<CatalogueItemEntity> optionalItem = itemRepository.findById(itemId);

        if (optionalCart.isEmpty() || optionalItem.isEmpty()) {
            return Optional.empty();
        }

        final CartEntity cart = optionalCart.get();
        final CatalogueItemEntity item = optionalItem.get();

        final CartItemEntity itemInCart = cart.findCartItemWithId(item.id());
        if (itemInCart == null) {
            cart.addItem(item, 1);
        } else {
            itemInCart.adjustQuantityBy(1);
        }

        final CartEntity savedCart = cartRepository.save(cart);
        return Optional.of(savedCart);
    }
}

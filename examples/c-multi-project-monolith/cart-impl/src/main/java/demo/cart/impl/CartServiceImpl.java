package demo.cart.impl;

import demo.cart.papi.Cart;
import demo.cart.papi.CartItem;
import demo.cart.papi.CartService;
import demo.catalogue.papi.CatalogueItem;
import demo.catalogue.papi.CatalogueItemService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CatalogueItemService itemService;

    public CartServiceImpl(final CartRepository cartRepository,
                           final CatalogueItemService itemService) {
        this.cartRepository = requireNonNull(cartRepository);
        this.itemService = requireNonNull(itemService);
    }

    @Override
    public Optional<Cart> findWithId(final long id) {
        return cartRepository.findById(id)
                .map(CartEntity::toDomain)
                .map(this::fetchCartItemsCaption);
    }

    @Override
    @Transactional
    public Optional<Cart> addItemToCart(final long cartId, final long itemId) {
        final Optional<CartEntity> optionalCart = cartRepository.findById(cartId);
        final Optional<CatalogueItem> optionalItem = itemService.findWithId(itemId);

        if (optionalCart.isEmpty() || optionalItem.isEmpty()) {
            return Optional.empty();
        }

        final CartEntity cart = optionalCart.get();
        final CatalogueItem item = optionalItem.get();

        final CartItemEntity itemInCart = cart.findCartItemWithId(item.id());
        if (itemInCart == null) {
            cart.addItem(item.id(), 1);
        } else {
            itemInCart.adjustQuantityBy(1);
        }

        final CartEntity savedCart = cartRepository.save(cart);
        return Optional.of(savedCart.toDomain())
                .map(this::fetchCartItemsCaption);
    }

    private Cart fetchCartItemsCaption(final Cart cart) {
        final List<CartItem> items = cart.items();
        final List<Long> cartItemIds = items.stream().map(CartItem::id).toList();

        final Map<Long, CatalogueItem> catalogueItems = itemService.findAllWithId(cartItemIds)
                .stream()
                .collect(Collectors.toMap(CatalogueItem::id, Function.identity()));

        final List<CartItem> withCaption = items.stream().map(i -> Optional.ofNullable(catalogueItems.get(i.id()))
                        .map(a -> i.withCaption(a.caption()))
                        .orElse(i))
                .toList();

        return cart.withItems(withCaption);
    }
}

package demo.cart.impl;

import demo.cart.papi.Cart;
import demo.cart.papi.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService service;

    public CartController(final CartService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> get(@PathVariable(value = "id") final long id) {
        return service.findWithId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{cartId}/item/{itemId}")
    public ResponseEntity<Cart> addItemToCart(@PathVariable(value = "cartId") final long cartId,
                                              @PathVariable(value = "itemId") final long itemId) {
        return service.addItemToCart(cartId, itemId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

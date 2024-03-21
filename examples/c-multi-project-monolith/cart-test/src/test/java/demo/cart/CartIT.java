package demo.cart;

import demo.cart.papi.Cart;
import demo.cart.papi.CartItem;
import demo.catalogue.papi.CatalogueItem;
import demo.catalogue.papi.CatalogueItemService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CatalogueItemService itemService;

    @Nested
    class GetCartTest {

        @Test
        void returnTheCartWithItemsWhenGivenAnExistingId() {
            when(itemService.findAllWithId(eq(List.of(1L, 5L))))
                    .thenReturn(List.of(
                            new CatalogueItem(1L, "Leather Sofa", ""),
                            new CatalogueItem(5L, "LED TV", "")));

            final ResponseEntity<Cart> response = getCartWithId(1);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);
            assertThat(response.getBody())
                    .isEqualTo(new Cart(1L, List.of(
                            new CartItem(1L, "Leather Sofa", 1),
                            new CartItem(5L, "LED TV", 1)
                    )));
        }

        @Test
        void returnNotFoundWhenGivenAnIdThatDoesNotExists() {
            final ResponseEntity<Cart> response = getCartWithId(10);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        private ResponseEntity<Cart> getCartWithId(final long id) {
            return restTemplate.getForEntity("/cart/%d".formatted(id), Cart.class);
        }
    }

    @Nested
    class AddItemToCartTest {

        @Test
        void returnTheCartWithTheNewItemWhenTheItemIsNotAlreadyInTheCart() {
            when(itemService.findWithId(eq(4L)))
                    .thenReturn(Optional.of(new CatalogueItem(4L, "Mug", "")));
            when(itemService.findAllWithId(eq(List.of(2L, 3L, 4L))))
                    .thenReturn(List.of(
                            new CatalogueItem(2L, "Wooden Table", ""),
                            new CatalogueItem(3L, "Plastic Chair", ""),
                            new CatalogueItem(4L, "Mug", "")));

            final ResponseEntity<Cart> response = addItemToCart(2, 4);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);
            assertThat(response.getBody())
                    .isEqualTo(new Cart(2L, List.of(
                            new CartItem(2L, "Wooden Table", 1),
                            new CartItem(3L, "Plastic Chair", 6),
                            new CartItem(4L, "Mug", 1)
                    )));
        }

        @Test
        void returnTheCartWithTheNewQuantityWhenTheItemIsAlreadyInTheCart() {
            when(itemService.findWithId(eq(4L)))
                    .thenReturn(Optional.of(new CatalogueItem(4L, "Mug", "")));
            when(itemService.findAllWithId(eq(List.of(4L))))
                    .thenReturn(List.of(new CatalogueItem(4L, "Mug", "")));

            final ResponseEntity<Cart> response = addItemToCart(3, 4);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);
            assertThat(response.getBody())
                    .isEqualTo(new Cart(3L, List.of(
                            new CartItem(4L, "Mug", 5)
                    )));
        }

        @Test
        void returnNotFoundWhenGivenACartIdThatDoesNotExists() {
            final ResponseEntity<Cart> response = addItemToCart(10, 1);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void returnNotFoundWhenGivenAnItemIdThatDoesNotExists() {
            when(itemService.findWithId(eq(10L)))
                    .thenReturn(Optional.empty());

            final ResponseEntity<Cart> response = addItemToCart(1, 10);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        private ResponseEntity<Cart> addItemToCart(final long cartId, final long itemId) {
            return restTemplate.postForEntity("/cart/%d/item/%d".formatted(cartId, itemId), Map.of(), Cart.class);
        }
    }
}

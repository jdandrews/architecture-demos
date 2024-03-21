package demo.catalogue.test;

import demo.catalogue.papi.CatalogueItem;
import demo.catalogue.papi.NewCatalogueItem;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogueItemIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class GetCatalogueItemTest {

        @Test
        void returnTheCatalogueItemWhenGivenAnExistingId() {
            final ResponseEntity<CatalogueItem> response = getCatalogueItemWithId(1);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.OK);
            assertThat(response.getBody())
                    .isEqualTo(new CatalogueItem(1L, "Leather Sofa", "A very nice and comfortable sofa"));
        }

        @Test
        void returnNotFoundWhenGivenAnIdThatDoesNotExists() {
            final ResponseEntity<CatalogueItem> response = getCatalogueItemWithId(10);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }

        private ResponseEntity<CatalogueItem> getCatalogueItemWithId(final long id) {
            return restTemplate.getForEntity("/catalogue/item/%d".formatted(id), CatalogueItem.class);
        }
    }

    @Nested
    class AddCatalogueItemTest {

        @Test
        void returnTheNewIdOfTheAddedCatalogueItem() {
            final NewCatalogueItem item = new NewCatalogueItem("Green Plant", "Put a little life in your living room!!");

            final ResponseEntity<CatalogueItem> response = restTemplate.postForEntity("/catalogue/item", item, CatalogueItem.class);
            assertThat(response.getStatusCode())
                    .isEqualTo(HttpStatus.CREATED);

            final long id = assertThatHeaderLocationHasCatalogueItemId(response);

            assertThat(response.getBody())
                    .isEqualTo(new CatalogueItem(id, item.caption(), item.description()));
        }

        private static long assertThatHeaderLocationHasCatalogueItemId(final ResponseEntity<CatalogueItem> response) {
            assertThat(response.getHeaders())
                    .isNotNull();
            assertThat(response.getHeaders().getLocation())
                    .isNotNull();

            final String location = response.getHeaders().getLocation().getPath();
            assertThat(location)
                    .matches(Pattern.compile("/catalogue/item/\\d"));

            return Long.parseLong(location.substring(16));
        }
    }
}

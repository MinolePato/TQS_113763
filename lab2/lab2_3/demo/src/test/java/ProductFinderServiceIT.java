import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.Product;
import com.example.ProductFinderService;
import com.example.SimpleHttpClient;
;


public class ProductFinderServiceIT {

    SimpleHttpClient httpClient = new SimpleHttpClient();
    ProductFinderService service = new ProductFinderService(httpClient);

    @Test
    public void whenGetExistingProduct_thenOk() throws IOException {
        Optional<Product> product = service.findProductDetail(1);
        assertTrue(product.isPresent());
        assertEquals(1, product.get().getId());
        assertEquals(product.get().getTitle(), "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops");
    }

}
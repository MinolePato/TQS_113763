import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.example.*;;


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
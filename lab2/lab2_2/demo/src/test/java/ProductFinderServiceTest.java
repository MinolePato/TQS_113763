import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ISimpleHttpClient;
import com.example.Product;
import com.example.ProductFinderService;

public class ProductFinderServiceTest {
    private ISimpleHttpClient mockHttpClient;
    private ProductFinderService productFinderService;

    @BeforeEach
    void setUp() {
        mockHttpClient = Mockito.mock(ISimpleHttpClient.class);
        productFinderService = new ProductFinderService(mockHttpClient);
    }

    @Test
    void testFindProductDetails() throws Exception {

        String mockJsonResponse = "{"
                + "\"id\": 1,"
                + "\"image\": \"https://example.com/image.jpg\","
                + "\"description\": \"A great product.\","
                + "\"price\": 19.99,"
                + "\"title\": \"Product Title\","
                + "\"category\": \"Electronics\""
                + "}";

        when(mockHttpClient.doHttpGet("https://api.example.com/products/1"))
                .thenReturn(mockJsonResponse);

        Product product = productFinderService.findProductDetails(1);
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("https://example.com/image.jpg", product.getImage());
        assertEquals("A great product.", product.getDescription());
        assertEquals(19.99, product.getPrice());
        assertEquals("Product Title", product.getTitle());
        assertEquals("Electronics", product.getCategory());

        verify(mockHttpClient, times(1)).doHttpGet("https://api.example.com/products/1");
    }
    @Test
    void testFindProductDetails_ValidProduct() {
        String mockResponse = "{"
                + "\"id\": 3,"
                + "\"image\": \"https://example.com/jacket.jpg\","
                + "\"description\": \"A stylish cotton jacket.\","
                + "\"price\": 55.99,"
                + "\"title\": \"Mens Cotton Jacket\","
                + "\"category\": \"Clothing\""
                + "}";

        when(mockHttpClient.doHttpGet("https://api.example.com/products/3")).thenReturn(mockResponse);

        Product product = productFinderService.findProductDetails(3);

        assertNotNull(product, "Esperava um produto, mas recebeu null");
        assertEquals(3, product.getId());
        assertEquals("Mens Cotton Jacket", product.getTitle());
        assertEquals("A stylish cotton jacket.", product.getDescription());
        assertEquals(55.99, product.getPrice());
        assertEquals("https://example.com/jacket.jpg", product.getImage());
        assertEquals("Clothing", product.getCategory());

        verify(mockHttpClient, times(1)).doHttpGet("https://api.example.com/products/3");
    }

    @Test
    void testFindProductDetails_ProductNotFound() {
        when(mockHttpClient.doHttpGet("https://api.example.com/products/300")).thenReturn("");

        Product product = productFinderService.findProductDetails(300);

        assertNull(product, "Esperava null, mas encontrou um produto");

        verify(mockHttpClient, times(1)).doHttpGet("https://api.example.com/products/300");
    }
}

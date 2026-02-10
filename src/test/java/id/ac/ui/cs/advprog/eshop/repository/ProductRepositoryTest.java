package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);

        Iterator<Product> iterator = productRepository.findAll();
        assertTrue(iterator.hasNext());

        Product savedProduct = iterator.next();
        assertNotNull(savedProduct.getProductId());
        assertEquals("Sampo Cap Bambang", savedProduct.getProductName());
        assertEquals(100, savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> iterator = productRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product p1 = new Product();
        p1.setProductName("Sampo Cap Bambang");
        p1.setProductQuantity(100);

        Product p2 = new Product();
        p2.setProductName("Sampo Cap Usep");
        p2.setProductQuantity(50);

        productRepository.create(p1);
        productRepository.create(p2);

        Iterator<Product> iterator = productRepository.findAll();

        assertTrue(iterator.hasNext());
        Product first = iterator.next();
        Product second = iterator.next();

        assertEquals("Sampo Cap Bambang", first.getProductName());
        assertEquals("Sampo Cap Usep", second.getProductName());
        assertFalse(iterator.hasNext());
    }


}

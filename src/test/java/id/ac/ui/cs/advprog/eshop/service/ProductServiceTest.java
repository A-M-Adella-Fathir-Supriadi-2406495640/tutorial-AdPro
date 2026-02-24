package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl(new ProductRepository());
    }

    @Test
    void testCreateAndFindAll() {
        Product product = new Product();
        product.setProductName("Test");
        product.setProductQuantity(10);

        service.create(product);

        List<Product> products = service.findAll();
        assertEquals(1, products.size());
        assertEquals("Test", products.get(0).getProductName());
    }

    @Test
    void testFindById() {
        Product product = new Product();
        product.setProductName("Test");
        product.setProductQuantity(10);

        service.create(product);
        String id = product.getProductId();

        Product found = service.findById(id);
        assertNotNull(found);
    }

    @Test
    void testUpdate() {
        Product product = new Product();
        product.setProductName("Test");
        product.setProductQuantity(10);

        service.create(product);
        product.setProductName("Updated");

        Product updated = service.update(product);
        assertEquals("Updated", updated.getProductName());
    }

    @Test
    void testDelete() {
        Product product = new Product();
        product.setProductName("Test");
        product.setProductQuantity(10);

        service.create(product);
        String id = product.getProductId();

        service.deleteById(id);

        assertNull(service.findById(id));
    }
}
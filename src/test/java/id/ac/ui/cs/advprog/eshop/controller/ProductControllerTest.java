package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import id.ac.ui.cs.advprog.eshop.model.Product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void testCreatePage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"));
    }

    @Test
    void testListPage() throws Exception {
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"));
    }

    @Test
    void testCreatePostRedirect() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "Test")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void testEditPage() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Test");
        product.setProductQuantity(10);

        Mockito.when(service.findById("1")).thenReturn(product);

        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"));
    }

    @Test
    void testEditProductNotFound() throws Exception {
        Mockito.when(service.findById("1")).thenReturn(null);

        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(post("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    void testEditPost() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "1")
                        .param("productName", "Updated")
                        .param("productQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }
}
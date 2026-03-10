package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    List<Order> orders;
    Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        orders = new ArrayList<>();
        orders.add(order);
    }

    // GET /order/create
    @Test
    void testGetCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"));
    }

    // GET /order/history
    @Test
    void testGetOrderHistoryPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistory"));
    }

    // POST /order/history
    @Test
    void testPostOrderHistoryFound() throws Exception {
        when(orderService.findAllByAuthor("Safira Sudrajat")).thenReturn(orders);

        mockMvc.perform(post("/order/history")
                        .param("author", "Safira Sudrajat"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistoryResult"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("author", "Safira Sudrajat"));
    }

    @Test
    void testPostOrderHistoryNotFound() throws Exception {
        when(orderService.findAllByAuthor("Unknown")).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/order/history")
                        .param("author", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistoryResult"))
                .andExpect(model().attribute("orders", new ArrayList<>()));
    }

    // GET /order/pay/{orderId}
    @Test
    void testGetPayOrderPage() throws Exception {
        when(orderService.findById(order.getId())).thenReturn(order);

        mockMvc.perform(get("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("PayOrder"))
                .andExpect(model().attributeExists("order"));
    }

    // POST /order/pay/{orderId}
    @Test
    void testPostPayOrder() throws Exception {
        when(orderService.findById(order.getId())).thenReturn(order);

        mockMvc.perform(post("/order/pay/" + order.getId())
                        .param("method", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("PayOrderResult"))
                .andExpect(model().attributeExists("paymentId"));
    }
}

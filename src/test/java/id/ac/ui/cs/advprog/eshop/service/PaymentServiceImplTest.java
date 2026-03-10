package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderService orderService;

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
    }

    // --- VOUCHER CODE ---

    @Test
    void testAddPaymentVoucherSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");

        Payment result = paymentService.addPayment(order, "VOUCHER", data);

        assertEquals("SUCCESS", result.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), "SUCCESS");
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidLength() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP123");  // kurang dari 16 karakter

        Payment result = paymentService.addPayment(order, "VOUCHER", data);

        assertEquals("REJECTED", result.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), "FAILED");
    }

    @Test
    void testAddPaymentVoucherNotStartWithESHOP() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ABCDE1234ABC5678");

        Payment result = paymentService.addPayment(order, "VOUCHER", data);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherInsufficientDigits() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABCDEFG"); // hanya 4 digit, butuh 8

        Payment result = paymentService.addPayment(order, "VOUCHER", data);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherNullCode() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", null);

        Payment result = paymentService.addPayment(order, "VOUCHER", data);

        assertEquals("REJECTED", result.getStatus());
    }

    // --- BANK TRANSFER ---

    @Test
    void testAddPaymentBankTransferSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", "REF123456");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", data);

        assertEquals("SUCCESS", result.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), "SUCCESS");
    }

    @Test
    void testAddPaymentBankTransferEmptyBankName() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "");
        data.put("referenceCode", "REF123456");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", data);

        assertEquals("REJECTED", result.getStatus());
        verify(orderService, times(1)).updateStatus(order.getId(), "FAILED");
    }

    @Test
    void testAddPaymentBankTransferNullBankName() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", null);
        data.put("referenceCode", "REF123456");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", data);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferEmptyReferenceCode() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", "");

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", data);

        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferNullReferenceCode() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", null);

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", data);

        assertEquals("REJECTED", result.getStatus());
    }

    // --- setStatus ---

    @Test
    void testSetStatusSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("pay-001", "VOUCHER", "WAITING_CONFIRMATION", data);
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        Map<String, String> data = new HashMap<>();
        Payment payment = new Payment("pay-002", "VOUCHER", "WAITING_CONFIRMATION", data);
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
    }

    // --- getPayment ---

    @Test
    void testGetPaymentFound() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", "REF123");
        Payment payment = new Payment("pay-001", "BANK_TRANSFER", "SUCCESS", data);
        doReturn(payment).when(paymentRepository).findById("pay-001");

        Payment result = paymentService.getPayment("pay-001");
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById("nonexistent");
        assertThrows(NoSuchElementException.class, () -> paymentService.getPayment("nonexistent"));
    }

    // --- getAllPayments ---

    @Test
    void testGetAllPayments() {
        Map<String, String> d1 = new HashMap<>();
        d1.put("voucherCode", "ESHOP1234ABC5678");
        Map<String, String> d2 = new HashMap<>();
        d2.put("bankName", "BCA");
        d2.put("referenceCode", "REF123");

        doReturn(List.of(
                new Payment("pay-001", "VOUCHER", "SUCCESS", d1),
                new Payment("pay-002", "BANK_TRANSFER", "SUCCESS", d2)
        )).when(paymentRepository).findAll();

        List<Payment> results = paymentService.getAllPayments();
        assertEquals(2, results.size());
    }
}

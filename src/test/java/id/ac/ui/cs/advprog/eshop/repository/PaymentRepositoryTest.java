package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    PaymentRepository paymentRepository;
    Map<String, String> voucherData;
    Map<String, String> bankData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", "REF123456");
    }

    @Test
    void testSaveNewPayment() {
        Payment payment = new Payment("pay-001", "VOUCHER", "SUCCESS", voucherData);
        Payment result = paymentRepository.save(payment);

        assertEquals("pay-001", result.getId());
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testSaveUpdatePayment() {
        Payment payment = new Payment("pay-001", "VOUCHER", "WAITING_CONFIRMATION", voucherData);
        paymentRepository.save(payment);

        Payment updated = new Payment("pay-001", "VOUCHER", "REJECTED", voucherData);
        paymentRepository.save(updated);

        assertEquals("REJECTED", paymentRepository.findById("pay-001").getStatus());
        assertEquals(1, paymentRepository.findAll().size());
    }

    @Test
    void testFindByIdFound() {
        Payment payment = new Payment("pay-001", "VOUCHER", "SUCCESS", voucherData);
        paymentRepository.save(payment);

        Payment result = paymentRepository.findById("pay-001");
        assertNotNull(result);
        assertEquals("pay-001", result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        Payment result = paymentRepository.findById("nonexistent");
        assertNull(result);
    }

    @Test
    void testFindAllReturnsAll() {
        paymentRepository.save(new Payment("pay-001", "VOUCHER", "SUCCESS", voucherData));
        paymentRepository.save(new Payment("pay-002", "BANK_TRANSFER", "SUCCESS", bankData));

        List<Payment> results = paymentRepository.findAll();
        assertEquals(2, results.size());
    }

    @Test
    void testFindAllEmpty() {
        assertTrue(paymentRepository.findAll().isEmpty());
    }
}

package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    Map<String, String> voucherData;
    Map<String, String> bankData;

    @BeforeEach
    void setUp() {
        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", "REF123456");
    }

    @Test
    void testCreatePaymentWithDefaultStatus() {
        Payment payment = new Payment("pay-001", "VOUCHER", voucherData);
        assertEquals("pay-001", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("WAITING_CONFIRMATION", payment.getStatus());
        assertEquals(voucherData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithCustomStatus() {
        Payment payment = new Payment("pay-001", "VOUCHER", "SUCCESS", voucherData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransfer() {
        Payment payment = new Payment("pay-002", "BANK_TRANSFER", "SUCCESS", bankData);
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("BCA", payment.getPaymentData().get("bankName"));
        assertEquals("REF123456", payment.getPaymentData().get("referenceCode"));
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("pay-001", "VOUCHER", voucherData);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("pay-001", "VOUCHER", voucherData);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testPaymentDataNotNull() {
        Payment payment = new Payment("pay-001", "VOUCHER", voucherData);
        assertNotNull(payment.getPaymentData());
    }
}

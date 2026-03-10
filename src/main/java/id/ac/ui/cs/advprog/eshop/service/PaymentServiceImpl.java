package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String status = determineStatus(method, paymentData);
        Payment payment = new Payment(UUID.randomUUID().toString(), method, status, paymentData);
        paymentRepository.save(payment);

        if ("SUCCESS".equals(status)) {
            orderService.updateStatus(order.getId(), "SUCCESS");
        } else if ("REJECTED".equals(status)) {
            orderService.updateStatus(order.getId(), "FAILED");
        }

        return payment;
    }

    private String determineStatus(String method, Map<String, String> paymentData) {
        return switch (method) {
            case "VOUCHER" -> isVoucherValid(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
            case "BANK_TRANSFER" -> isBankTransferValid(paymentData) ? "SUCCESS" : "REJECTED";
            default -> "WAITING_CONFIRMATION";
        };
    }

    private boolean isVoucherValid(String code) {
        if (code == null) return false;
        if (code.length() != 16) return false;
        if (!code.startsWith("ESHOP")) return false;
        long digitCount = code.chars()
                .filter(Character::isDigit)
                .count();
        return digitCount == 8;
    }

    // Method baru yang di-extract dari determineStatus
    private boolean isBankTransferValid(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");
        return bankName != null && !bankName.isEmpty()
                && referenceCode != null && !referenceCode.isEmpty();
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) throw new NoSuchElementException();
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}

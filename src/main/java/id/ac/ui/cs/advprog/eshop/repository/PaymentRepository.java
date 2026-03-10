package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {
    private List<Payment> payments = new ArrayList<>();

    public Payment save(Payment payment) {
        Optional<Payment> existing = payments.stream()
                .filter(p -> p.getId().equals(payment.getId()))
                .findFirst();

        if (existing.isPresent()) {
            int index = payments.indexOf(existing.get());
            payments.set(index, payment);
        } else {
            payments.add(payment);
        }
        return payment;
    }

    public Payment findById(String id) {
        return payments.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }
}

package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order ;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    private List<Order> orderData = new ArrayList<>();

    public Order save(Order order) {
        // Cari apakah order dengan ID tersebut sudah ada
        for (int i = 0; i < orderData.size(); i++) {
            if (orderData.get(i).getId().equals(order.getId())) {
                // Jika ditemukan, update order tersebut
                orderData.set(i, order);
                return order;
            }
        }
        // Jika tidak ditemukan, tambahkan sebagai order baru
        orderData.add(order);
        return order;
    }

    public Order findById(String id) {
        for (Order order : orderData) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findAllByAuthor(String author) {
        // Menggunakan stream untuk filter berdasarkan author (case sensitive)
        return orderData.stream()
                .filter(order -> order.getAuthor().equals(author))
                .collect(Collectors.toList());
    }
}

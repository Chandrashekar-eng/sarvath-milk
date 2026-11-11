package com.example.demo.controller;

import com.example.demo.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final EmailService emailService;

    public OrderController(EmailService emailService) {
        this.emailService = emailService;
    }

    public record OrderItem(String name, String quantity, double price) {}

    public record OrderRequest(
        String customerId,
        String customerName,
        String customerEmail,
        String phone,
        String deliverySlot,
        String address,
        List<OrderItem> items,
        double totalAmount
    ) {}

    @PostMapping
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody OrderRequest request) {
        String orderId = "SAR-ORD-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        // Trigger automatic Resend email in the background
        emailService.sendOrderConfirmation(request, orderId);

        return ResponseEntity.ok(Map.of(
            "status", "success",
            "orderId", orderId
        ));
    }
}

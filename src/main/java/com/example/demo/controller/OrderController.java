package com.example.demo.controller;

import com.example.demo.CustomerOrder;
import com.example.demo.CustomerOrderRepository;
import com.example.demo.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;
import java.time.Duration;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    private final EmailService emailService;
    private final CustomerOrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderController(EmailService emailService, CustomerOrderRepository orderRepository, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    public record OrderItem(String name, String quantity, double price) {}
    public record OrderRequest(String customerId, String customerName, String customerEmail, String phone,
        String deliverySlot, String deliveryAt, String address, List<OrderItem> items, double totalAmount) {}

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        if (request == null || blank(request.customerId()) || blank(request.customerName()) ||
            blank(request.customerEmail()) || blank(request.phone()) || blank(request.address()) ||
            request.items() == null || request.items().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message","Please provide customer details, address and at least one item."));
        }
        try {
            Instant scheduled = Instant.parse(request.deliveryAt());
            if (Duration.between(Instant.now(), scheduled).toHours() < 8)
                return ResponseEntity.badRequest().body(Map.of("status","error","message","Delivery must be scheduled at least 8 hours after placing the order."));
        } catch (Exception invalidDate) {
            return ResponseEntity.badRequest().body(Map.of("status","error","message","A valid delivery time is required."));
        }
        String orderId = "SAR-ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        try {
            CustomerOrder saved = new CustomerOrder();
            saved.setOrderId(orderId);
            saved.setCustomerId(request.customerId());
            saved.setCustomerName(request.customerName());
            saved.setCustomerEmail(request.customerEmail());
            saved.setPhone(request.phone());
            saved.setDeliverySlot(request.deliverySlot());
            saved.setDeliveryAt(request.deliveryAt());
            saved.setAddress(request.address());
            saved.setItemsJson(objectMapper.writeValueAsString(request.items()));
            saved.setTotalAmount(request.totalAmount());
            orderRepository.save(saved);
            emailService.sendOrderConfirmation(request, orderId);
            return ResponseEntity.ok(Map.of("status","success","orderId",orderId,"message","Order saved successfully"));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(Map.of("status","error","message","Unable to save order. Please try again."));
        }
    }

    @GetMapping("/history/{customerId}")
    public List<CustomerOrder> history(@PathVariable String customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    private boolean blank(String value) { return value == null || value.isBlank(); }
}

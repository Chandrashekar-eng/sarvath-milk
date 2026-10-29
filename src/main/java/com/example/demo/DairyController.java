package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DairyController {

    private final ProductRepository productRepo;
    private final OrderInquiryRepository inquiryRepo;
    private final NotificationService notificationService;

    public DairyController(ProductRepository productRepo, 
                           OrderInquiryRepository inquiryRepo, 
                           NotificationService notificationService) {
        this.productRepo = productRepo;
        this.inquiryRepo = inquiryRepo;
        this.notificationService = notificationService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @PostMapping("/inquiry")
    public ResponseEntity<String> submitInquiry(@RequestBody OrderInquiry inquiry) {
        // 1. Save the order details into the database
        OrderInquiry savedOrder = inquiryRepo.save(inquiry);

        // 2. Automatically dispatch the confirmation email
        notificationService.sendOrderNotifications(savedOrder);

        // 3. Return a success response to your frontend
        return ResponseEntity.ok("Order #" + savedOrder.getId() + " placed successfully! Confirmation email sent to " + savedOrder.getEmail());
    }
}